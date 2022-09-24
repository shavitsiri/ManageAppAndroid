package com.example.ManageYourSelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ManageYourSelf.databinding.ActivityListJobToDoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListJobToDo extends AppCompatActivity {


    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef, refToJobDone, databaseReference;
    String name, em;

    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<String> arrayListKey = new ArrayList<>();
    ArrayList<jobToDo> arrayJobToDoOBJ = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    TextView numJobToDo;
    int countJobToDo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_job_to_do);

        // *** auth area *** ///
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users").child(mAuth.getUid());
        String temp = mAuth.getCurrentUser().getEmail();
        String [] parts = temp.split("@");
        em = parts[0];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);
                name = value.getFullName();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // *** JobsToDo area *** ///
        databaseReference = FirebaseDatabase.getInstance().getReference("JobsToDo").child(em);
        refToJobDone = FirebaseDatabase.getInstance().getReference("JobsDone").child(em);
        ListView chl = (ListView) findViewById(R.id.ListViewTxt);
        numJobToDo = (TextView)findViewById(R.id.numJobToDo);
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.row_layout_list_view,R.id.txt_lan,arrayList);
        chl.setAdapter(arrayAdapter);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(jobToDo.class).toString();
                arrayList.add(value);
                GenericTypeIndicator<ArrayList<jobToDo>> t = new GenericTypeIndicator<ArrayList<jobToDo>>() {};
                arrayJobToDoOBJ.add(snapshot.getValue(jobToDo.class));
                arrayListKey.add(snapshot.getKey());
                arrayAdapter.notifyDataSetChanged();
                countJobToDo = arrayAdapter.getCount();
                numJobToDo.setText("מס' עבודות לביצוע: " + arrayAdapter.getCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = (int)id;
                String name = "";
                String selectedItem = ((TextView)view).getText().toString();
                new AlertDialog.Builder(ListJobToDo.this)
                        .setTitle("מחיקה")
                        .setMessage("האם אתה בטוח שסיימת את העבודה?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList.remove(i);
                                String keyToDelete  = arrayListKey.get(i);
                                databaseReference.child(name).child(keyToDelete).removeValue();
                                arrayListKey.remove(i);
                                arrayAdapter.notifyDataSetChanged();
                                countJobToDo--;
                                numJobToDo.setText("מס' עבודות לביצוע: " + countJobToDo);
                                refToJobDone.child(arrayJobToDoOBJ.get(i).getId()).setValue(arrayJobToDoOBJ.get(i));
                            }
                        })
                        .setNegativeButton("לא", null)
                        .show();

            }
        });
    }
}
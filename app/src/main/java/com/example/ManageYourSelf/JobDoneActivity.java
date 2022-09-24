package com.example.ManageYourSelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobDoneActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef, refToJobDone;
    String name;
    String em;

    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<String> arrayListKey = new ArrayList<>();
    ArrayList<jobToDo> arrayJobToDoOBJ = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    TextView numJobsDone;
    int countJobsDone = 0;
    Button BtnGoBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_done);

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

        // *** JobsDone area *** ///
        BtnGoBackToHome =findViewById(R.id.BtnGoBackToHome);
        refToJobDone = FirebaseDatabase.getInstance().getReference("JobsDone").child(em);
        ListView chl = (ListView) findViewById(R.id.ListViewTxt);
        numJobsDone = (TextView)findViewById(R.id.numJobsDone);
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.row_layout_list_view,R.id.txt_lan,arrayList);
        chl.setAdapter(arrayAdapter);
        refToJobDone.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(jobToDo.class).toString();
                arrayList.add(value);
                GenericTypeIndicator<ArrayList<jobToDo>> t = new GenericTypeIndicator<ArrayList<jobToDo>>() {};
                arrayJobToDoOBJ.add(snapshot.getValue(jobToDo.class));
                arrayListKey.add(snapshot.getKey());
                arrayAdapter.notifyDataSetChanged();
                countJobsDone = arrayAdapter.getCount();
                numJobsDone.setText("מס' עבודות : " + arrayAdapter.getCount());
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
                new AlertDialog.Builder(JobDoneActivity.this)
                        .setTitle("מחיקה")
                        .setMessage("אתה בטוח שברצונך למחוק סיום עבודה זה?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList.remove(i);
                                String keyToDelete  = arrayListKey.get(i);
                                refToJobDone.child(name).child(keyToDelete).removeValue();
                                arrayListKey.remove(i);
                                arrayAdapter.notifyDataSetChanged();
                                countJobsDone--;
                                numJobsDone.setText("מס'  עבודות: " + countJobsDone);
                            }
                        })
                        .setNegativeButton("לא", null)
                        .show();

            }
        });
        BtnGoBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(JobDoneActivity.this, menuPage.class);
//                startActivity(intent);
                startActivity(new Intent(JobDoneActivity.this,menuPage.class));
            }
        });
    }

}
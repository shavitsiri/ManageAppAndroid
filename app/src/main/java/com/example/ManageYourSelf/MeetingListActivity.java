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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MeetingListActivity extends AppCompatActivity {

    FirebaseDatabase database; // מקבל את ההפנייה ל-fireBase
    FirebaseAuth mAuth; // הפנייה לאוטנטיקיישן
    DatabaseReference myRef; // הפניה למשתמשים
    String name; // השם של המשתמש המחובר
    String em; // מקבל את המחרוזת של האימייל עד לפני השטרודל

    ArrayList<String> selectedItems = new ArrayList<>(); // מערך מחרוזת לתפיסת הפגישות המסומנות

    DatabaseReference databaseReference; // הפניה לפגישות
    ArrayList<String> arrayList = new ArrayList<>(); // רשימה להצגת הפגישות
    ArrayList<String> arraylistkey = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter; // אדפטר
    TextView numMeetToDo; // טקסטויוו לכמות הפגישות שיש לבצע
    int countMeetingToDo = 0; // צובר לכמות הפגישות שיש לבצע



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

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

        // *** meeting area *** ///
        databaseReference = FirebaseDatabase.getInstance().getReference("Meetings").child(em);
        ListView chl = (ListView) findViewById(R.id.checkable_list);
        numMeetToDo = (TextView)findViewById(R.id.numMeetToDo);
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.row_layout_list_view,R.id.txt_lan,arrayList);
        chl.setAdapter(arrayAdapter);
        databaseReference.addChildEventListener((new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(Meet.class).toString();
                arrayList.add(value);
                arraylistkey.add(snapshot.getKey());
                arrayAdapter.notifyDataSetChanged();
                countMeetingToDo = arrayAdapter.getCount();
                numMeetToDo.setText("מס' פגישות לביצוע: " + arrayAdapter.getCount());
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
        }));

        chl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = (int)id;
                String name = "";
                String selectedItem = ((TextView)view).getText().toString();
                new AlertDialog.Builder(MeetingListActivity.this)
                        .setTitle("מחיקה")
                        .setMessage("האם אתה בטוח שברצונך למחוק את הפגישה מהיומן?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList.remove(i);
                                String keyToDelete  = arraylistkey.get(i);
                                databaseReference.child(name).child(keyToDelete).removeValue();
                                arraylistkey.remove(i);
                                arrayAdapter.notifyDataSetChanged();
                                countMeetingToDo--;
                                numMeetToDo.setText("מס' פגישות לביצוע: " + countMeetingToDo);
                            }
                        })
                        .setNegativeButton("לא", null)
                        .show();
            }
        });
    }


    public void showSelectedItems(View view){
        String meetings = "";
        for(String meet:selectedItems){
            meetings += meet;
        }

    }

}
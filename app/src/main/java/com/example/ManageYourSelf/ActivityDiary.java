package com.example.ManageYourSelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class ActivityDiary extends AppCompatActivity {

    DatabaseReference dbRefJtd, dbRefMeet;
    CalendarView calendarView;
    TextView myDate;
    Button pickDate;
    ArrayList<String> missions = new ArrayList<>();
    Dialog mDialog;


    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    String name;
    String em;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);



        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users").child(mAuth.getUid());
        String temp = mAuth.getCurrentUser().getEmail();
        String [] parts = temp.split("@");
        em = parts[0];

        dbRefJtd = FirebaseDatabase.getInstance().getReference("JobsToDo").child(em);
        dbRefMeet = FirebaseDatabase.getInstance().getReference("Meetings").child(em);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView) findViewById(R.id.myDate);
        pickDate = (Button) findViewById(R.id.pickDate);
        mDialog = new Dialog(this);




        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year ;
                myDate.setText(date);

            }
        });


        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String mission : missions){
                    if(mission.contains(myDate.getText().toString()) == true){
                        if(mission.contains(("מחיר"))){
                            String data =  mission.toString();
                            Intent transferText = new Intent(ActivityDiary.this,popUp.class);
                            transferText.putExtra("abc", data);
                            startActivity(transferText);
                            finishActivity(R.layout.activity_pop_up);
                            finish();
                            return;
                        }
                        String data =  mission.toString();
                        Intent transferText = new Intent(ActivityDiary.this,popUp.class);
                        transferText.putExtra("abc", data);
                        startActivity(transferText);
                        finishActivity(R.layout.activity_pop_up);
                        finish();
//                        openPopUpWindow();
                        return;
                    }
                }
                Toast.makeText(ActivityDiary.this,   " יום פנוי :)", Toast.LENGTH_LONG).show();
            }
        });

        dbRefMeet.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(Meet.class).toString();
                missions.add(value);
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

        dbRefJtd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(jobToDo.class).toString();
                missions.add(value);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for(DataSnapshot data: snapshot.getChildren()){
                    if(snapshot.child("clientDate").getValue().toString() == myDate.getText().toString()){
                        Toast.makeText(ActivityDiary.this, snapshot.child("clientDate").getValue().toString()  + "אירוע : ", Toast.LENGTH_SHORT).show();
                    }
                }
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


    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(ActivityDiary.this,menuPage.class));
    }

    private void openPopUpWindow() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_pop_up);
        dialog.show();
    }
}
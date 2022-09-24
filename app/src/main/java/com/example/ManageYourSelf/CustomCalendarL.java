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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CustomCalendarL extends AppCompatActivity {
    CustomCalendar customCalendar;
    DatabaseReference dbRefJtd, dbRefMeet;
    CalendarView calendarView;
    TextView myDate;
    Button pickDate;
    ArrayList<String> missions = new ArrayList<>();
    Dialog mDialog;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    String em;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calendar_l);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        try{
            myRef = database.getReference("Users").child(mAuth.getUid()) ;
        }
        catch(Exception e){
            Toast.makeText(CustomCalendarL.this, "wrong", Toast.LENGTH_SHORT).show();
        }

        String temp = mAuth.getCurrentUser().getEmail();
        String [] parts = temp.split("@");
        em = parts[0];
        dbRefJtd = FirebaseDatabase.getInstance().getReference("JobsToDo").child(em);
        dbRefMeet = FirebaseDatabase.getInstance().getReference("Meetings").child(em);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView) findViewById(R.id.myDate);
        pickDate = (Button) findViewById(R.id.pickDate);
        mDialog = new Dialog(this);

        // assign variable
        customCalendar = findViewById(R.id.custom_calendar);

        // Initialize description hashmap
        HashMap<Object, Property> descHashMap=new HashMap<>();

        // Initialize default property
        Property defaultProperty=new Property();

        // Initialize default resource
        defaultProperty.layoutResource = R.layout.default_view;

        // Initialize and assign variable
        defaultProperty.dateTextViewResource = R.id.text_view;

        // Put object and property
        descHashMap.put("default",defaultProperty);

        // for current date
        Property currentProperty=new Property();
        currentProperty.layoutResource=R.layout.current_view;
        currentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("current",currentProperty);

        // for present date
        Property presentProperty=new Property();
        presentProperty.layoutResource=R.layout.present_view;
        presentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("present",presentProperty);

        // For absent
        Property absentProperty =new Property();
        absentProperty.layoutResource=R.layout.absent_view;
        absentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("absent",absentProperty);

        // set desc hashmap on custom calendar
        customCalendar.setMapDescToProp(descHashMap);

        // Initialize date hashmap
        HashMap<Integer,Object> dateHashmap=new HashMap<>();

        // initialize calendar
        Calendar calendar =  Calendar.getInstance();

        // Put values
        dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH),"current");

        // set date
        customCalendar.setDate(calendar,dateHashmap);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                // get string date
                String sDate=selectedDate.get(Calendar.DAY_OF_MONTH)
                        +"/" +(selectedDate.get(Calendar.MONTH)+1)
                        +"/" + selectedDate.get(Calendar.YEAR);

                for (String mission: missions){
                    if(mission.contains(sDate)){
                        if(mission.contains("מחיר")){
                            Intent transferText = new Intent(CustomCalendarL.this,popUp.class);
                            transferText.putExtra("abc", mission);
                            startActivity(transferText);
                            finishActivity(R.layout.activity_pop_up);
                            finish();
                            return;
                        }
                        Intent transferText = new Intent(CustomCalendarL.this,popUp.class);
                        transferText.putExtra("abc", mission);
                        startActivity(transferText);
                        finishActivity(R.layout.activity_pop_up);
                        finish();
//                        openPopUpWindow();
                        return;
                    }
                }
                Toast.makeText(CustomCalendarL.this,   " יום פנוי :)", Toast.LENGTH_LONG).show();
            }
        });

        dbRefMeet.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(Meet.class).toString();
                missions.add(value);
                int pos = value.indexOf("תאריך:");
                String str[] = value.substring(pos).split("/");
                str[0] = str[0].substring(7);
                int day = Integer.parseInt(str[0]);
                int month = Integer.parseInt(str[1]);
                Calendar cal = Calendar.getInstance();
                int currMonth = cal.get(Calendar.MONTH)+1;
                //Toast.makeText(CustomCalendarL.this, "monthmine: " + month +"monthApi= " + currMonth, Toast.LENGTH_SHORT).show();
                if(month == currMonth){
                    dateHashmap.put(day, "present");
                    customCalendar.setDate(calendar, dateHashmap);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        dbRefJtd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(jobToDo.class).toString();
                missions.add(value);
                int pos = value.indexOf("תאריך:");
                String str[] = value.substring(pos).split("/");
                str[0] = str[0].substring(7);
                int day = Integer.parseInt(str[0]);
                int month = Integer.parseInt(str[1]);
                Calendar cal = Calendar.getInstance();
                int currMonth = cal.get(Calendar.MONTH)+1;
                //Toast.makeText(CustomCalendarL.this, "monthmine: " + month +"monthApi= " + currMonth, Toast.LENGTH_SHORT).show();
                if(month == currMonth){
                    dateHashmap.put(day, "absent");
                    customCalendar.setDate(calendar, dateHashmap);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot data: snapshot.getChildren()){
                    if(snapshot.child("clientDate").getValue().toString() == myDate.getText().toString()){
                        Toast.makeText(CustomCalendarL.this, snapshot.child("clientDate").getValue().toString()  + "אירוע : ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }



    @Override
    public void onBackPressed(){
        startActivity(new Intent(CustomCalendarL.this,menuPage.class));
    }

    private void openPopUpWindow() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_pop_up);
        dialog.show();
    }
}
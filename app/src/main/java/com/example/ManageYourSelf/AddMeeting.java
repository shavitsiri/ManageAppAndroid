package com.example.ManageYourSelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddMeeting extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextView mDisplayDate, chosen_date, tvDate, TxtWrong;
    String date;

    EditText ClientName, ClientAddress, ClientPhone, ClientMeetDate;
    Boolean firstTime = true;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        mAuth = FirebaseAuth.getInstance();

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddMeeting.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + (month+1) + "/" + year  ;
                tvDate.setText("לשינוי לחץ כאן");
                chosen_date.setText("התאריך שנבחר: " + date   );
            }
        };

        Button BtnAddMeeting = (Button) findViewById(R.id.BtnAddMeeting);

        BtnAddMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeeting();
            }
        });
        InitViews();
        InitVarbs();
    }

    private void InitVarbs() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(mAuth.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("Meetings");
    }

    private void InitViews() {
        ClientName = findViewById(R.id.ClientName);
        ClientAddress = findViewById(R.id.ClientAddress);
        ClientPhone = findViewById(R.id.ClientPhone);
        chosen_date = findViewById(R.id.chosen_date);
        tvDate = findViewById(R.id.tvDate);
        TxtWrong = findViewById(R.id.TxtWrong);
    }


    private void addMeeting() {
        firstTime = true;
        Meet m = new Meet();
        m.setClientName(ClientName.getText().toString());
        m.setClientAddress(ClientAddress.getText().toString());
        m.setClientPhone(ClientPhone.getText().toString());
        m.setClientMeetDate(date);
        String id = databaseReference.push().getKey();
        m.setId(id);


        if (m.getClientName().equals("") || m.getClientAddress().equals("") || m.getClientPhone().equals("") || m.getClientMeetDate().equals("")) {
            TxtWrong.setText("* אחד או יותר מהשדות ריקים או שגויים");
            return;
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);
                String currentUser = value.getEMail();
                String [] parts = currentUser.split("@");
                String em = parts[0];
                databaseReference.child(em).child(m.getId()).setValue(m);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        firstTime = false;
        startActivity(new Intent(AddMeeting.this,MeetingListActivity.class));
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}

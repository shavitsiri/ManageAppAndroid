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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddJobToDoActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextView mDisplayDate, chosen_date,tvDate, TxtWrongAddJob;
    String date;
    int idToJob = 0;
    EditText ClientName, ClientPhoneNumber, ClientAddress, ClientMeters, ClientPricePerMeter, ClientParquet;
    Boolean firstTime = true;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_to_do);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mAuth = FirebaseAuth.getInstance();
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddJobToDoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + (month+1) + "/" + year  ;
                tvDate.setText("לשינוי לחץ כאן");
                chosen_date.setText("התאריך שנבחר: " + date );
            }
        };

        Button BtnAddNewJob = (Button) findViewById(R.id.BtnAddNewJob);


        BtnAddNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJobToDo();
            }
        });

        InitViews();
        InitVarbs();
    }

    private void InitVarbs() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(mAuth.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("JobsToDo");
    }



    private void InitViews() {
        ClientName = findViewById(R.id.ClientName);
        ClientPhoneNumber = findViewById(R.id.ClientPhoneNumber);
        ClientAddress = findViewById(R.id.ClientAddress);
        chosen_date = findViewById(R.id.chosen_date);
        ClientMeters = findViewById(R.id.ClientMeters);
        ClientPricePerMeter = findViewById(R.id.ClientPricePerMeter);
        ClientParquet = findViewById(R.id.ClientParquet);
        tvDate = findViewById(R.id.tvDate);
        TxtWrongAddJob = findViewById(R.id.TxtWrongAddJob);
    }

    private void addJobToDo() {
        firstTime = true;
        jobToDo j = new jobToDo();
        j.setClientName(ClientName.getText().toString());
        j.setClientAddress(ClientAddress.getText().toString());
        j.setClientPhoneNumber(ClientPhoneNumber.getText().toString());
        j.setClientDate(date);
        j.setClientParquet(ClientParquet.getText().toString());
        j.setClientMeters(ClientMeters.getText().toString());
        j.setClientPricePerMeter(ClientPricePerMeter.getText().toString());
        String id = databaseReference.push().getKey();
        j.setId(id);

        if (j.getClientName().equals("") || j.getClientAddress().equals("") || j.getClientPhoneNumber().equals("") || j.getClientDate().equals("")  || j.getClientMeters().equals("") || j.getClientPricePerMeter().equals("")) {
            TxtWrongAddJob.setText("* אחד או יותר מהשדות ריקים או שגויים");
            return;
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);
                String currentUser = value.getEMail();
                String [] parts = currentUser.split("@");
                String em = parts[0];
                databaseReference.child(em).child(j.getId()).setValue(j);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(AddJobToDoActivity.this, "העבודה נוספה בהצלחה ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddJobToDoActivity.this,ListJobToDo.class));
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
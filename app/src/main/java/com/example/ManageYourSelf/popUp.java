package com.example.ManageYourSelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class popUp extends AppCompatActivity {

    Button BtnBackToCalendar;
    TextView kindEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        BtnBackToCalendar = findViewById(R.id.BtnBackToCalendar);
        kindEvent = findViewById(R.id.kindEvent);


        BtnBackToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(popUp.this,CustomCalendarL.class));
            }
        });

        TextView header = findViewById(R.id.header);

        Bundle bn = getIntent().getExtras();
        String kind = bn.getString("abc");
        header.setText(String.valueOf(kind));
        String txt = header.getText().toString();
        if(txt.contains("מחיר")){
            kindEvent.setText("עבודה");
        }
        else {
            kindEvent.setText("פגישה");
        }
    }
}
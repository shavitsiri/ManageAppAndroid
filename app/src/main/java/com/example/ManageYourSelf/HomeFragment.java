package com.example.ManageYourSelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class HomeFragment extends Fragment  {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    ImageView BtnLogout;


    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        Button BtnGoToAddJob = (Button)view.findViewById(R.id.BtnGoToAddJob);
        Button BtnGoToAddMeeting = (Button)view.findViewById(R.id.BtnGoToAddMeeting);
        Button BtnGoToListMeeting = (Button)view.findViewById(R.id.BtnGoToListMeeting);
        Button BtnGoTOListJobToDo = (Button)view.findViewById(R.id.BtnGoTOListJobToDo);
//        Button BtnGoToCalendar = (Button)view.findViewById(R.id.BtnGoToCalendar);
        Button BtnGoToJobsDone = (Button)view.findViewById(R.id.BtnGoToJobsDone);
        Button BtnGoToCalendarNew = (Button)view.findViewById(R.id.BtnGoToCalendarNew);
        TextView txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        BtnLogout = (ImageView) view.findViewById(R.id.BtnLogout);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(mAuth.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);
                txtUserName.setText(value.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BtnGoToCalendarNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CustomCalendarL.class);
                startActivity(i);
            }
        });


        BtnGoToAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddJobToDoActivity.class);
                startActivity(i);
            }
        });

        BtnGoToAddMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddMeeting.class);
                startActivity(i);
            }
        });
        BtnGoToListMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MeetingListActivity.class);
                startActivity(i);
            }
        });

        BtnGoTOListJobToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListJobToDo.class);
                startActivity(i);
            }
        });

//        BtnGoToCalendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), ActivityDiary.class);
//                startActivity(i);
//            }
//        });

        BtnGoToJobsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), JobDoneActivity.class);
                startActivity(i);
            }
        });

        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("התנתקות")
                        .setMessage("אתה בטוח שאתה רוצה להתנתק?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("לא", null)
                        .show();
            }
        });


        return view;
    }

}
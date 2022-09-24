package com.example.ManageYourSelf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    String em;
    FirebaseDatabase database;
    DatabaseReference myRef, databaseReferenceMeets, databaseReferenceJobsToDo, databaseReferenceJobsDone;
    FirebaseAuth mAuth;
    TextView ProfileName, ProfileEmail, MeetingToDo, countJobs, countDone;

    int countMeetingToDo = 0, countJobToDo = 0, countJobsDone = 0;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users").child(mAuth.getUid());
        String temp = mAuth.getCurrentUser().getEmail();
        String [] parts = temp.split("@");
        em = parts[0];
        ProfileName = (TextView) view.findViewById(R.id.ProfileName);
        ProfileEmail = (TextView) view.findViewById(R.id.ProfileEmail);
        MeetingToDo = (TextView) view.findViewById(R.id.MeetingToDo);
        countJobs = (TextView) view.findViewById(R.id.countJobs);
        countDone = (TextView) view.findViewById(R.id.countDone);
        databaseReferenceJobsDone = FirebaseDatabase.getInstance().getReference("JobsDone").child(em);
        databaseReferenceJobsToDo = FirebaseDatabase.getInstance().getReference("JobsToDo").child(em);
        databaseReferenceMeets = FirebaseDatabase.getInstance().getReference("Meetings").child(em);
        databaseReferenceMeets.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                countMeetingToDo++;
                MeetingToDo.setText(countMeetingToDo+ " ");
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

        databaseReferenceJobsToDo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                countJobToDo++;
                countJobs.setText(countJobToDo+ " ");
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

        databaseReferenceJobsDone.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                countJobsDone++;
                countDone.setText(countJobsDone+ " ");
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);
                ProfileName.setText( value.getFullName());
                ProfileEmail.setText(value.getEMail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
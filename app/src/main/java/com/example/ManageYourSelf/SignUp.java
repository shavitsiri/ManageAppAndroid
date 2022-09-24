package com.example.ManageYourSelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    // אתחול המשתנים של המחלקה
    EditText SignUpEmailInput, SignUpUserNameInput, SignUpPasswordInput, SignUpConfirmPasswordInput;
    TextView wrongInput;
    Button BtnSignUp;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        InitViews();
        InitButtons();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
    }

    private void InitButtons() {
        BtnSignUp = findViewById(R.id.BtnGoToLogin);
        BtnSignUp.setOnClickListener(this);
    }

    private void InitViews() {
        SignUpEmailInput = findViewById(R.id.SignUpEmailInput);
        SignUpPasswordInput = findViewById(R.id.SignUpPasswordInput);
        SignUpConfirmPasswordInput = findViewById(R.id.SignUpConfirmPasswordInput);
        SignUpUserNameInput = findViewById(R.id.SignUpUserNameInput);
        wrongInput = findViewById(R.id.wrongInput);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BtnGoToLogin:
                signUpAction();
                break;
        }
    }

    private void signUpAction() {
        String userEmail, userPassword, userRepass;
        userEmail = SignUpEmailInput.getText().toString();
        userPassword = SignUpPasswordInput.getText().toString();
        userRepass = SignUpConfirmPasswordInput.getText().toString();
        User user = new User(SignUpUserNameInput.getText().toString(), SignUpEmailInput.getText().toString() );
        if(userEmail.isEmpty() || userPassword.isEmpty() || userRepass.isEmpty()){
            wrongInput.setText("* אחד או יותר מהפרטים הזנת ריק או שגוי");
            return;
        }
        if (userPassword.equals(userRepass)) {
            mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                myRef.child(mAuth.getUid()).setValue(user);
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(SignUp.this, task.getException().toString() + "registration failed!/",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            Toast.makeText(SignUp.this, "you have signed up successfully", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(SignUp.this, "those Passwords doesn't match!!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}








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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView wrongInput;
    Button BtnLogin, BtnGoToSignUp;
    EditText LoginEmailInput,LoginPasswordInput;
    String userEmail,userPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitViews();
        InitButtons();
    }

    private void InitVarbs(){
        userEmail = LoginEmailInput.getText().toString();
        userPassword = LoginPasswordInput.getText().toString();
        mAuth = FirebaseAuth.getInstance();
    }

    private void InitButtons(){
        BtnGoToSignUp.setOnClickListener(this);
        BtnLogin.setOnClickListener(this);
    }

    private void InitViews(){
        BtnLogin = findViewById(R.id.BtnLogin);
        BtnGoToSignUp = findViewById(R.id.BtnGoToSignUp);
        LoginEmailInput = findViewById(R.id.LoginEmailInput);
        LoginPasswordInput = findViewById(R.id.LoginPasswordInput);
        wrongInput = findViewById(R.id.wrongInput);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.BtnGoToSignUp:
                startActivity(new Intent(MainActivity.this,SignUp.class));
                break;

            case R.id.BtnLogin:
                MakeLogin();
                break;
        }
    }

    private void MakeLogin() {
        InitVarbs();
        if (userEmail.isEmpty() || userPassword.isEmpty()){
            wrongInput.setText("* אחד או יותר מהפרטים הזנת ריק או שגוי");
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(MainActivity.this,menuPage.class));
                        }
                        else{
                            wrongInput.setText("* אחד או יותר מהפרטים הזנת ריק או שגוי");
                        }
                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
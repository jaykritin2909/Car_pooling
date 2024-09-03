package com.dataflair.carpooling.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dataflair.carpooling.Model.MainActivity;
import com.dataflair.carpooling.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DatabaseReference mDatabase;

    private EditText emailEditText, passwordEditText;
    private Button registerButton;

    String Emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        progressDialog =new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(!email.matches(Emailpattern))
        {
            emailEditText.setError("enter correct email");
        } else if (password.isEmpty() || password.length()<6)
        {
            passwordEditText.setError("Enter proper Password");

        }else{
            progressDialog.setMessage("please wait while we register...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success
                            progressDialog.dismiss();
                            sendUsertoNextActivity();
                            Toast.makeText(RegisterActivity.this,"REGISTRATION SUCCESSFULL",Toast.LENGTH_SHORT);

                        } else {
                            progressDialog.dismiss();
                            // Registration failed
                            Toast.makeText(RegisterActivity.this, "Registration failed! User already exist,please Login.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendUsertoNextActivity() {
        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}

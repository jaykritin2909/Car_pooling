package com.dataflair.carpooling.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dataflair.carpooling.Model.MainActivity;
import com.dataflair.carpooling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


    public class LoginActivity extends AppCompatActivity {


        // Declare FirebaseAuth object
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        EditText emailEditText, passwordEditText;
        Button loginButton;
        String Emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        ProgressDialog progressDialog;
        Button createnewAccount;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            createnewAccount=findViewById(R.id.createnewAccount);

           getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            mUser=mAuth.getCurrentUser();
            progressDialog =new ProgressDialog(this);
            // Find views
             emailEditText = findViewById(R.id.emailEditText);
             passwordEditText = findViewById(R.id.passwordEditText);
             loginButton = findViewById(R.id.loginButton);

            // Set click listener for register button
            createnewAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the registration activity when the button is clicked
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performlogin();

                    }

            });
        }

        private void performlogin() {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!email.matches(Emailpattern)) {
                emailEditText.setError("enter correct email");
            } else if (password.isEmpty() || password.length() < 6) {
                passwordEditText.setError("Enter proper Password");

            } else {
                progressDialog.setMessage("please wait while we login..");
                progressDialog.setTitle("login");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sendUsertoNextActivity();
                            Toast.makeText(LoginActivity.this, "Login SUCCESSFULL", Toast.LENGTH_SHORT);
                        }else
                        {
                            progressDialog.dismiss();
                            // Registration failed
                            Toast.makeText(LoginActivity.this, "Login failed, Please Register if you are a New User!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
            private void sendUsertoNextActivity() {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


    }


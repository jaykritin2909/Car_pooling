package com.dataflair.carpooling.Model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dataflair.carpooling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhoneNumber, editTextAadharNumber, editTextBloodGroup, editTextGender;
    private Button buttonSave;
    private TextView textViewName, textViewPhoneNumber, textViewAadharNumber, textViewBloodGroup, textViewGender;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextAadharNumber = findViewById(R.id.editTextAadharNumber);
        editTextBloodGroup = findViewById(R.id.editTextBloodGroup);
        editTextGender = findViewById(R.id.editTextGender);
        buttonSave = findViewById(R.id.buttonSave);

        textViewName = findViewById(R.id.editTextName);
        textViewPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        textViewAadharNumber = findViewById(R.id.editTextAadharNumber);
        textViewBloodGroup = findViewById(R.id.editTextBloodGroup);
        textViewGender = findViewById(R.id.editTextGender);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                        String aadharNumber = dataSnapshot.child("aadharNumber").getValue().toString();
                        String bloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                        String gender = dataSnapshot.child("gender").getValue().toString();

                        textViewName.setText(name);
                        textViewPhoneNumber.setText(phoneNumber);
                        textViewAadharNumber.setText(aadharNumber);
                        textViewBloodGroup.setText(bloodGroup);
                        textViewGender.setText(gender);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void saveUserProfile() {
        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String aadharNumber = editTextAadharNumber.getText().toString().trim();
        String bloodGroup = editTextBloodGroup.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();

        if (!name.isEmpty() && !phoneNumber.isEmpty() && !aadharNumber.isEmpty() && !bloodGroup.isEmpty() && !gender.isEmpty()) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                userRef.child("name").setValue(name);
                userRef.child("phoneNumber").setValue(phoneNumber);
                userRef.child("aadharNumber").setValue(aadharNumber);
                userRef.child("bloodGroup").setValue(bloodGroup);
                userRef.child("gender").setValue(gender);

                Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ProfileActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}

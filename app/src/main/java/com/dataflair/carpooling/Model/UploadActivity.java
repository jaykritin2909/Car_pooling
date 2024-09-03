

package com.dataflair.carpooling.Model;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.dataflair.carpooling.R;

public class UploadActivity extends AppCompatActivity {

    Button CreateRideBtn,BacktoHome;

    DatabaseReference mdatabase;

    EditText RiderNameEdit, SourceAddressEdit, DestinationAddressEdit, TotalPassengersEdit, RidePriceEdit, RideDateEdit, RideTimeEdit, PhoneNumberEdit, viaroute, aadharnumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        RiderNameEdit = findViewById(R.id.RiderNameEdit);
        SourceAddressEdit = findViewById(R.id.SourceAddressEdit);
        DestinationAddressEdit = findViewById(R.id.DestinationAddressEdit);
        TotalPassengersEdit = findViewById(R.id.TotalPassengersEdit);
        RideDateEdit = findViewById(R.id.RideDateEdit);
        RideTimeEdit = findViewById(R.id.RideTimeEdit);
        RidePriceEdit = findViewById(R.id.RidePriceEdit);
        PhoneNumberEdit = findViewById(R.id.PhoneNumberEdit);
        aadharnumber = findViewById(R.id.aadharnumber);
        viaroute = findViewById(R.id.viaroute);
        CreateRideBtn = findViewById(R.id.CreateRideBtn);
        BacktoHome=findViewById(R.id.BackToHome);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Rides");

        BacktoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to home
                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        CreateRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadData();


            }
        });

    }

    public void uploadData() {


        String name = RiderNameEdit.getText().toString();
        String source = SourceAddressEdit.getText().toString();
        String dest = DestinationAddressEdit.getText().toString();
        String total = TotalPassengersEdit.getText().toString();
        String date = RideDateEdit.getText().toString();
        String time = RideTimeEdit.getText().toString();
        String price = RidePriceEdit.getText().toString();
        String phone = PhoneNumberEdit.getText().toString();
        String aadhar = aadharnumber.getText().toString();
        String via = viaroute.getText().toString();


        Rides rides = new Rides(name, source, dest, total, date, time, price, phone, aadhar, via);

        mdatabase.push().setValue(rides);
        Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UploadActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


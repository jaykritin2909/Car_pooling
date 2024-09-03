

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
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.dataflair.carpooling.R;

public class UploadActivity extends AppCompatActivity {

    Button CreateRideBtn;
    EditText RiderNameEdit,SourceAddressEdit,DestinationAddressEdit,TotalPassengersEdit,RidePriceEdit,RideDateEdit,RideTimeEdit,PhoneNumberEdit,viaroute,aadharnumber;
    Uri uri;

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
        RidePriceEdit= findViewById(R.id.RidePriceEdit);
        PhoneNumberEdit = findViewById(R.id.PhoneNumberEdit);
        aadharnumber = findViewById(R.id.aadharnumber);
        viaroute = findViewById(R.id.viaroute);
        CreateRideBtn = findViewById(R.id.CreateRideBtn);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri=data.getData();
                        }else {
                            Toast.makeText(UploadActivity.this, "no image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        CreateRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
            }
        });

    }

    public void savedata(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Image").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progess_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadData(){

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

        DataClass dataClass = new DataClass(name, source, dest, total, date, time, price, phone, aadhar, via);

        FirebaseDatabase.getInstance().getReference("Rides").child(name).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
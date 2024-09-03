package com.dataflair.carpooling.Model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dataflair.carpooling.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SOSActivity extends AppCompatActivity {

    private EditText etContact1, etRelationship1, etContact2, etRelationship2, etContact3, etRelationship3;
    private Button btnSaveContacts, btnSetAlert;
    private DatabaseReference databaseReference;
    private LocationManager locationManager;
    private String currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        etContact1 = findViewById(R.id.etContact1);
        etRelationship1 = findViewById(R.id.etRelationship1);
        etContact2 = findViewById(R.id.etContact2);
        etRelationship2 = findViewById(R.id.etRelationship2);
        etContact3 = findViewById(R.id.etContact3);
        etRelationship3 = findViewById(R.id.etRelationship3);
        btnSaveContacts = findViewById(R.id.btnSaveContacts);
        btnSetAlert = findViewById(R.id.btnSetAlert);

        databaseReference = FirebaseDatabase.getInstance().getReference("EmergencyContacts");

        loadContacts();

        btnSaveContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContacts();
            }
        });

        btnSetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSOS();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void loadContacts() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EmergencyContact contact = snapshot.getValue(EmergencyContact.class);
                    if (contact != null) {
                        if (contact.getId() == 1) {
                            etContact1.setText(contact.getPhoneNumber());
                            etRelationship1.setText(contact.getRelationship());
                        } else if (contact.getId() == 2) {
                            etContact2.setText(contact.getPhoneNumber());
                            etRelationship2.setText(contact.getRelationship());
                        } else if (contact.getId() == 3) {
                            etContact3.setText(contact.getPhoneNumber());
                            etRelationship3.setText(contact.getRelationship());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SOSActivity.this, "Failed to load contacts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveContacts() {
        EmergencyContact contact1 = new EmergencyContact(1, etContact1.getText().toString(), etRelationship1.getText().toString());
        EmergencyContact contact2 = new EmergencyContact(2, etContact2.getText().toString(), etRelationship2.getText().toString());
        EmergencyContact contact3 = new EmergencyContact(3, etContact3.getText().toString(), etRelationship3.getText().toString());

        databaseReference.child("1").setValue(contact1);
        databaseReference.child("2").setValue(contact2);
        databaseReference.child("3").setValue(contact3);

        Toast.makeText(this, "Contacts saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void sendSOS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SmsManager smsManager = SmsManager.getDefault();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        EmergencyContact contact = snapshot.getValue(EmergencyContact.class);
                        if (contact != null) {
                            String message = "I am in an emergency. My current location is:  http://maps.google.com/maps?q=13.118974095742887,77.66067622602674";
                            smsManager.sendTextMessage(contact.getPhoneNumber(), null, message, null, null);
                        }
                    }
                    Toast.makeText(SOSActivity.this, "SOS sent successfully!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SOSActivity.this, "Failed to send SOS", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            currentLocation = "<a href=\"http://maps.google.com/maps?q=13.118974095742887,77.66067622602674\">13.118974095742887, 77.66067622602674</a>";
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(@NonNull String provider) { }

        @Override
        public void onProviderDisabled(@NonNull String provider) { }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }
}

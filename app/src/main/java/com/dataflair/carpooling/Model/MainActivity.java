package com.dataflair.carpooling.Model;
/**
 * We Performs the Bottom Navigation in this activity
 * we will check whether user logged in or not and redirect to Starting activity if not login
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataflair.carpooling.Activities.RegisterActivity;
import com.dataflair.carpooling.Adapter.RideAdapter;
import com.dataflair.carpooling.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DatabaseReference mDatabase;
    EditText sourceEditText ;
    EditText destinationEditText ;
    RecyclerView recyclerView;
    RideAdapter rideAdapter;
    BottomNavigationView mbottomNavigationView;

    Button buttonBookRide;

    ArrayList<Rides> ridesList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sourceEditText = findViewById(R.id.sourceEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        fab=findViewById(R.id.fab);
        mbottomNavigationView = findViewById(R.id.BottomNavigationView);
        ridesList = new ArrayList<>();



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Rides> options =
                new FirebaseRecyclerOptions.Builder<Rides>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rides"), Rides.class)
                        .build();
        rideAdapter = new RideAdapter(MainActivity.this, ridesList);
        recyclerView.setAdapter(rideAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Rides");
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });


//assigning the bottom navigation to navigate between the fragments
        mbottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
        Menu menuNav = mbottomNavigationView.getMenu();


        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryRides();
            }
        });
        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeMenu:
                        // Handle home menu item click
                        Toast.makeText(MainActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.emergencyMenu:
                        // Handle create ride menu item click
                        Toast.makeText(MainActivity.this, "Emergency SOS clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, SOSActivity.class));
                        return true;
                    case R.id.profileMenu:
                        // Handle profile menu item click
                        Toast.makeText(MainActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    private void queryRides() {
        String source = sourceEditText.getText().toString().trim();
        String destination = destinationEditText.getText().toString().trim();

        // Debugging: Print the values of source and destination to logcat
        Log.d("MainActivity", "Source: " + source);
        Log.d("MainActivity", "Destination: " + destination);

// Debugging: Display source and destination in a toast message
        Toast.makeText(MainActivity.this, "Source: " + source + ", Destination: " + destination, Toast.LENGTH_SHORT).show();

        Query query= FirebaseDatabase.getInstance().getReference("Rides").orderByChild("source").equalTo(source);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("MainActivity", "onDataChange triggered");
                List<Rides> ridesList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rides ride = snapshot.getValue(Rides.class);
                    if (ride!=null){
                        Log.d("MainActivity", "Ride retrieved: " + ride.getName() + ", Destination: " + ride.getDest());
                        if ( ride.getDest() != null && ride.getDest().equals(destination)) {
                            Log.d("MainActivity", "Ride added to filtered list");
                            ridesList.add(ride);
                    }else {
                            Log.d("MainActivity", "Ride filtered out, Destination does not match");
                        }
                    }else{
                        Log.e("MainActivity", "Failed to retrieve ride from snapshot");
                    }
                }
                if(!ridesList.isEmpty()) {
                    rideAdapter.setRidesList(ridesList);
                    rideAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(MainActivity.this, "No rides found for destination: " + destination, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error fetching data: " + databaseError.getMessage());

                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        rideAdapter.startListening();
        //checks the user is loggedIn or not
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            //Redirect to the starting Activity if the user is not logged
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        rideAdapter.stopListening();
    }

}
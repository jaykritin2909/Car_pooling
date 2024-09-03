package com.dataflair.carpooling.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dataflair.carpooling.Model.MainActivity;
import com.dataflair.carpooling.Model.Rides;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.carpooling.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RiderViewHolder> {

    private Context mContext;
    private ArrayList<Rides> ridesList;



    public RideAdapter(Context mContext, ArrayList<Rides> ridesList) {
        this.mContext = mContext;
        this.ridesList = ridesList;
    }

    @NonNull
    @Override
    public RideAdapter.RiderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_item_layout, parent, false);
        return new RiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideAdapter.RiderViewHolder holder, int position) {
        Rides ride = ridesList.get(position);
        holder.mRiderNameTxt.setText(ride.getName());
        holder.mRiderAadharTxt.setText(ride.getAadhar());
        holder.mRiderViaTxt.setText(ride.getVia());
        holder.mtotalpassengersTxt.setText(ride.getTotal());
        holder.mSourceAddressTxt.setText(ride.getSource());
        holder.mDestinationAddressTxt.setText(ride.getDest());
        holder.mRideDateTxt.setText(ride.getDate());
        holder.mRideTimeTxt.setText(ride.getTime());
        holder.mRidePriceTxt.setText(ride.getPrice());
        holder.mRiderPhoneNumberTxt.setText(ride.getPhone());
        holder.bind(ride);
    }
      @Override
      public int getItemCount(){
        return ridesList.size();
      }

    public void startListening() {
    }

    public void stopListening() {
    }

    public class RiderViewHolder extends RecyclerView.ViewHolder {

        private TextView mRiderNameTxt, mRiderAadharTxt,mRiderViaTxt,mtotalpassengersTxt,mSourceAddressTxt, mDestinationAddressTxt, mRideDateTxt, mRideTimeTxt, mRidePriceTxt, mRiderPhoneNumberTxt;
        private Button mBookRideBtn;

        public RiderViewHolder(@NonNull View itemView) {
            super(itemView);
            mRiderNameTxt = itemView.findViewById(R.id.textRiderNameValue);
            mSourceAddressTxt = itemView.findViewById(R.id.textSourceAddressValue);
            mDestinationAddressTxt = itemView.findViewById(R.id.textDestinationAddressValue);
            mtotalpassengersTxt=itemView.findViewById(R.id.textTotalPassengersValue);
            mRideDateTxt = itemView.findViewById(R.id.textRideDateValue);
            mRideTimeTxt = itemView.findViewById(R.id.textRideTimeValue);
            mRidePriceTxt = itemView.findViewById(R.id.textRidePriceValue);
            mRiderPhoneNumberTxt = itemView.findViewById(R.id.textPhoneNumberValue);
            mRiderAadharTxt=itemView.findViewById(R.id.textAadharNumberValue);
            mRiderViaTxt=itemView.findViewById(R.id.textViaRouteValue);


            mBookRideBtn = itemView.findViewById(R.id.buttonBookRide);

            mBookRideBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show toast message
                    Toast.makeText(itemView.getContext(), "Ride Booked and redirecting to WhatsApp", Toast.LENGTH_SHORT).show();
                    String phoneNumber = mRiderPhoneNumberTxt.getText().toString();
                    String text = "Hello, I have booked the ride you created, please share your live location to track";
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://wa.me/" + phoneNumber + "?text=" + text));
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        // Catch any exception that occurs if WhatsApp is not installed or if there's an issue with the URI
                        Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        public void bind(Rides ride) {
            Log.d("RideAdapter", "Rider Name: " + ride.getName());
            mRiderNameTxt.setText(ride.getName());
            Log.d("RideAdapter", "Source Address: " + ride.getSource());
            mSourceAddressTxt.setText(ride.getSource());
            mRiderViaTxt.setText(ride.getVia());
            mDestinationAddressTxt.setText(ride.getDest());
            mtotalpassengersTxt.setText(ride.getTotal());
            mRiderAadharTxt.setText(ride.getAadhar());
            mRideDateTxt.setText(ride.getDate());
            mRideTimeTxt.setText(ride.getTime());
            mRidePriceTxt.setText(ride.getPrice());
            mRiderPhoneNumberTxt.setText(ride.getPhone());
        }


    }
    private boolean isAppInstalled(String packageName) {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public void setRidesList(List<Rides> ridesList) {
        this.ridesList = (ArrayList<Rides>) ridesList;
        notifyDataSetChanged();
    }
}

package com.dataflair.carpooling.Model;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.carpooling.R;

public class RideViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView sourceTextView;
    private TextView destinationTextView;

    private TextView totalTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView phoneTextView;

    private TextView priceTextView;
    private TextView aadharTextView;
    private TextView viaTextView;


    private Button bookRideButton;

    public RideViewHolder(@NonNull View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.textRiderNameValue);
        sourceTextView = itemView.findViewById(R.id.textSourceAddressValue);
        destinationTextView = itemView.findViewById(R.id.textDestinationAddressValue);
        totalTextView=itemView.findViewById(R.id.textTotalPassengersValue);
        dateTextView=itemView.findViewById(R.id.textRideDateValue);
        timeTextView=itemView.findViewById(R.id.textRideTimeValue);
        phoneTextView=itemView.findViewById(R.id.textPhoneNumberValue);
        priceTextView=itemView.findViewById(R.id.textRidePriceValue);
        aadharTextView=itemView.findViewById(R.id.textAadharNumberValue);
        viaTextView=itemView.findViewById(R.id.textViaRouteValue);

        bookRideButton = itemView.findViewById(R.id.buttonBookRide);
    }

    public void setDetails(Rides ride) {
        nameTextView.setText(ride.getName());
        sourceTextView.setText(ride.getSource());
        destinationTextView.setText(ride.getDest());
        totalTextView.setText(ride.getTotal());
        dateTextView.setText(ride.getDate());
        timeTextView.setText(ride.getTime());
        phoneTextView.setText(ride.getPhone());
        priceTextView.setText(ride.getPrice());
        aadharTextView.setText(ride.getAadhar());
        viaTextView.setText(ride.getVia());


    }
}

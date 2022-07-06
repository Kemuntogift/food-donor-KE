package com.pro.fooddonorke.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.fooddonorke.ui.Donation;
import com.pro.fooddonorke.utilities.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseDonationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseDonationViewHolder(@NonNull View itemView, View mView, Context mContext) {
        super(itemView);
        this.mView = itemView;
        this.mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindDonations(){

    }

    @Override
    public void onClick(View view) {

        // declare the donations arraylists

        final ArrayList<Donation> donations = new ArrayList();

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_DONATIONS);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    donations.add(snapshot.getValue(Donation.class));

                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, Donation.class);

                intent.putExtra("position", itemPosition +"");
                intent.putExtra("donations", Parcels.wrap(donations));

                mContext.startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

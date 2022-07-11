package com.pro.fooddonorke.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.ui.OrganizationDetailActivity;
import com.pro.fooddonorke.utilities.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Objects;

public class FirebaseOrganizationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String TAG = FirebaseOrganizationViewHolder.class.getSimpleName();
    View mView;
    Context mContext;

    public FirebaseOrganizationViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mView.setOnClickListener(this);
    }

    public void bindRelief(Charity mRelief) {

        ImageView mOrganizationImageView = mView.findViewById(R.id.org_image);
        TextView mNameTextView = mView.findViewById(R.id.org_name);
        TextView mTypeTextView = mView.findViewById(R.id.org_type);
        TextView mLocationTextView = mView.findViewById(R.id.org_location);

        Glide.with(mContext).asBitmap().load(mRelief.getImage()).placeholder(R.drawable.ic_baseline_business).into(mOrganizationImageView);
        mNameTextView.setText(mRelief.getName());
        mTypeTextView.setText(mRelief.getType());
        mLocationTextView.setText(mRelief.getLocation());
    }

    @Override
    public void onClick(View view) {

        // Declare the donations arraylists
        final ArrayList<Charity> mRelief = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Objects.requireNonNull(user).getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_DONATIONS)
                .child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    mRelief.add(dataSnapshot.getValue(Charity.class));
                }

                int itemPosition = getLayoutPosition();

                // Open details activity
                Intent intent = new Intent(mContext, OrganizationDetailActivity.class);
                intent.putExtra("position", itemPosition);
                intent.putExtra("reliefs", Parcels.wrap(mRelief));
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error while fetching donations", error.toException());
            }
        });
    }
}

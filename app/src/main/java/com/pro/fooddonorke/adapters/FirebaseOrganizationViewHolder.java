package com.pro.fooddonorke.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.pro.fooddonorke.ui.Donation;
import com.pro.fooddonorke.utilities.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;

public class FirebaseOrganizationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseOrganizationViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindRelief(Charity mRelief) {

        ImageView mOrganizationImageView = (ImageView) mView.findViewById(R.id.org_image);
        TextView mNameTextView = (TextView) mView.findViewById(R.id.org_name);
        TextView mTypeTextView = (TextView) mView.findViewById(R.id.org_type);
        TextView mLocationTextView = (TextView) mView.findViewById(R.id.org_location);

        Glide.with(mContext).asBitmap().load(mRelief.getImage()).placeholder(R.drawable.ic_baseline_business).into(mOrganizationImageView);
        mNameTextView.setText(mRelief.getName());
        mTypeTextView.setText(mRelief.getType());
        mLocationTextView.setText(mRelief.getLocation());
    }

    @Override
    public void onClick(View view) {

        // declare the donations arraylists

        final ArrayList<Charity> mRelief = new ArrayList();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Objects.requireNonNull(user).getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_DONATIONS)
                .child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    mRelief.add(snapshot.getValue(Charity.class));

                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, Donation.class);

                intent.putExtra("position", itemPosition +"");
                intent.putExtra("reliefs", Parcels.wrap(mRelief));

                mContext.startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

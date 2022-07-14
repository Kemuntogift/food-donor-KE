package com.pro.fooddonorke.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.interfaces.ItemOnClickListener;
import com.pro.fooddonorke.models.DonationRequest;
import com.pro.fooddonorke.ui.OrganizationDetailActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationRequestAdapter extends RecyclerView.Adapter<DonationRequestAdapter.DonationViewHolder> {
    private List<DonationRequest> mDonations;
    private Context mContext;
    private ItemOnClickListener mListener;

    public DonationRequestAdapter(Context context, List<DonationRequest> donations, ItemOnClickListener listener) {
        mContext = context;
        mDonations = donations;
        mListener = listener;
    }

    @NonNull
    @Override
    public DonationRequestAdapter.DonationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_list, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationRequestAdapter.DonationViewHolder holder, int position) {
        DonationRequest donationRequest = mDonations.get(position);
        holder.bindDonation(donationRequest);
        holder.itemView.setOnClickListener(view -> mListener.openCharityDetails(donationRequest.getCharityId()));
    }

    @Override
    public int getItemCount() {
        return mDonations.size();
    }

    public class DonationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.org_name) TextView mNameTextView;
        @BindView(R.id.org_type) TextView mTypeTextView;
        @BindView(R.id.org_location) TextView mLocationTextView;
        @BindView(R.id.org_image)
        ShapeableImageView requestImage;

        private Context mContext;

        public DonationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }
        public void bindDonation(DonationRequest donation) {
            Glide.with(mContext).asBitmap().load(R.drawable.healthcare).into(requestImage);
            mNameTextView.setText(donation.getMessage());
            mTypeTextView.setText(donation.getCreatedAt());
            mLocationTextView.setText(donation.getLocation());
        }

        @Override
        public void onClick(View v) {

        }
    }
}


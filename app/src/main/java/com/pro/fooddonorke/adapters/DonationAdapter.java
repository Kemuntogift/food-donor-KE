package com.pro.fooddonorke.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.models.DonationRequest;
import com.pro.fooddonorke.models.RequestsSearchResponse;
import com.pro.fooddonorke.ui.OrganizationDetailActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {
    private List<DonationRequest> mDonations;
    private Context mContext;

    public DonationAdapter(Context context, List<DonationRequest> donations) {
        mContext = context;
        mDonations = donations;
    }

    @NonNull
    @Override
    public DonationAdapter.DonationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organization_list, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationAdapter.DonationViewHolder holder, int position) {
        holder.bindDonation(mDonations.get(position));
    }

    @Override
    public int getItemCount() {
        return mDonations.size();
    }

    public class DonationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.org_name) TextView mNameTextView;
        @BindView(R.id.org_type) TextView mTypeTextView;
        @BindView(R.id.org_location) TextView mLocationTextView;

        private Context mContext;

        public DonationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }
        public void bindDonation(DonationRequest donation) {
            mNameTextView.setText(donation.getMessage());
            mTypeTextView.setText(donation.getCreatedAt());
            mLocationTextView.setText(donation.getLocation());
        }
        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, OrganizationDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("donations", Parcels.wrap(mDonations));
            mContext.startActivity(intent);
        }
    }
}


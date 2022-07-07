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
import com.pro.fooddonorke.ui.OrganizationDetailActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrganizationListAdapter extends RecyclerView.Adapter<OrganizationListAdapter.OrganizationViewHolder> {
    private List<Charity> mReliefs;
    private Context mContext;

    public OrganizationListAdapter(Context context, List<Charity> reliefs) {
        mContext = context;
        mReliefs = reliefs;
    }

    @NonNull
    @Override
    public OrganizationListAdapter.OrganizationViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organization_list, parent, false);
        return new OrganizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrganizationListAdapter.OrganizationViewHolder holder, int position) {
        holder.bindRelief(mReliefs.get(position));
    }

    @Override
    public int getItemCount() {
        return mReliefs.size();
    }

    public class OrganizationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.org_image)
        ImageView mOrganizationImageView;
        @BindView(R.id.org_name)
        TextView mNameTextView;
        @BindView(R.id.org_type)
        TextView mTypeTextView;
        @BindView(R.id.org_location)
        TextView mLocationTextView;

        private Context mContext;

        public OrganizationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }
        public void bindRelief(Charity relief) {
            Glide.with(mContext).asBitmap().load(relief.getImage()).placeholder(R.drawable.ic_baseline_business).into(mOrganizationImageView);
            mNameTextView.setText(relief.getName());
            mTypeTextView.setText(relief.getType());
            mLocationTextView.setText(relief.getLocation());
        }
        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, OrganizationDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("reliefs", Parcels.wrap(mReliefs));
            mContext.startActivity(intent);
        }
    }
}

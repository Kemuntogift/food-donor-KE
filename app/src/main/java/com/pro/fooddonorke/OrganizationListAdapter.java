package com.pro.fooddonorke;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrganizationListAdapter extends RecyclerView.Adapter<OrganizationListAdapter.OrganizationViewHolder> {
    private List<String> charities;
    private Context mContext;

    public OrganizationListAdapter(Context context, List<String> charities) {
        mContext = context;
    }

    @NonNull
    @Override
    public OrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrganizationViewHolder extends RecyclerView.ViewHolder {
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

        }
    }
}

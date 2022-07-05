package com.pro.fooddonorke.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.divider.MaterialDivider;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.models.Charity;

import java.util.List;

import butterknife.BindView;


public class OrganizationDetailFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.org_image_detail) ImageView mOrganizationDetailImage;
    @BindView(R.id.org_name) TextView mOrganizationName;
    @BindView(R.id.org_icon) ImageView mOrganizationIcon;
    @BindView(R.id.org_type) TextView mOrganizationType;
    @BindView(R.id.location_icon) ImageView mLocationIcon;
    @BindView(R.id.org_location) TextView mOrganizationLocation;
    @BindView(R.id.phone_image) ImageView mPhoneImage;
    @BindView(R.id.twitter_image) ImageView mTwitterImage;
    @BindView(R.id.facebook_image) ImageView mFacebookImage;
    @BindView(R.id.instagram_image) ImageView mInstagramImage;
    @BindView(R.id.mail_image) ImageView mEmailImage;
    @BindView(R.id.divider) MaterialDivider mDivider;
    @BindView(R.id.food_donation_title) TextView mFoodDonationTitle;
    @BindView(R.id.food_donation_types) ChipGroup mFoodDonationType;
    @BindView(R.id.cardView) CardView mCardView;
    @BindView(R.id.brief_desc) TextView mBriefDescription;
    @BindView(R.id.org_desc_image_1) ImageView mImageOne;
    @BindView(R.id.org_desc_image_2) ImageView mImageTwo;
    @BindView(R.id.org_desc_image_3) ImageView mImageThree;
    @BindView(R.id.donateButton)
    Button mDonateButton;


    private Charity mRelief;

    public OrganizationDetailFragment() {
        // Required empty public constructor
    }


    public static OrganizationDetailFragment newInstance(String param1, String param2) {
        OrganizationDetailFragment fragment = new OrganizationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organization_detail, container, false);
    }
}
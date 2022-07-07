package com.pro.fooddonorke.fragment;

import static com.pro.fooddonorke.utilities.Constants.FIREBASE_CHILD_DONATIONS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.models.Charity;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.donateButton) Button mDonateButton;


    private Charity mRelief;

    public OrganizationDetailFragment() {
        // Required empty public constructor
    }


    public static OrganizationDetailFragment newInstance(Charity relief) {
        OrganizationDetailFragment fragment = new OrganizationDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("relief", Parcels.wrap(relief));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mRelief = Parcels.unwrap(getArguments().getParcelable("relief"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_organization_detail, container, false);
        ButterKnife.bind(this, view);

        // Set organization image
        Glide.with(requireContext()).asBitmap().load(mRelief.getImage()).placeholder(R.drawable.img).into(mOrganizationDetailImage);

        mOrganizationName.setText(mRelief.getName());
        mOrganizationType.setText(mRelief.getType());
        mOrganizationLocation.setText(mRelief.getLocation());
        mBriefDescription.setText(mRelief.getDescription());

        // Set food donation types
        for (String foodDonation: mRelief.getFooddonations()) {
            Chip chip = new Chip(requireContext());
            chip.setText(foodDonation);
            mFoodDonationType.addView(chip);
        }

        // Set brief description images
        List<String> descriptionImages = mRelief.getDescriptionImages();
        for (int i = 0; i < descriptionImages.size(); i++) {
            Glide.with(requireContext()).asBitmap().load(descriptionImages.get(0)).into(mImageOne);
            Glide.with(requireContext()).asBitmap().load(descriptionImages.get(1)).into(mImageTwo);
            Glide.with(requireContext()).asBitmap().load(descriptionImages.get(2)).into(mImageThree);
        }

        mDonateButton.setOnClickListener(this);
        mPhoneImage.setOnClickListener(this);
        mTwitterImage.setOnClickListener(this);
        mFacebookImage.setOnClickListener(this);
        mInstagramImage.setOnClickListener(this);
        mEmailImage.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        if (v == mDonateButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = Objects.requireNonNull(user).getUid();
            String pushId = String.format(Locale.ENGLISH,"relief_%d", mRelief.getId());
            DatabaseReference reliefRef = FirebaseDatabase
                    .getInstance()
                    .getReference(FIREBASE_CHILD_DONATIONS)
                    .child(uid).child(pushId);

            mRelief.setPushId(pushId);
            reliefRef.setValue(mRelief).addOnCompleteListener(requireActivity(), insertTask -> {
                if (insertTask.isSuccessful()){
                    Toast.makeText(getContext(), "Thanks for donating!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), Objects.requireNonNull(insertTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        // Set implicit intents
        if (v == mPhoneImage){
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mRelief.getContacts().getPhone()));
            startActivity(phoneIntent);
        }

        if (v == mTwitterImage){
            Intent twitterIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRelief.getContacts().getTwitter()));
            startActivity(twitterIntent);
        }

        if (v == mFacebookImage){
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRelief.getContacts().getFacebook()));
            startActivity(facebookIntent);
        }

        if (v == mInstagramImage){
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRelief.getContacts().getInstagram()));
            startActivity(instagramIntent);
        }

        if (v == mEmailImage){
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");   // Only email apps handle this intent
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mRelief.getContacts().getEmail()});
            startActivity(Intent.createChooser(emailIntent, "Choose an email client: "));
        }
    }
}
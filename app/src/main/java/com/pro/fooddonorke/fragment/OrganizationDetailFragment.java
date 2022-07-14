package com.pro.fooddonorke.fragment;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.dialog.DonationDetailsDialogFragment;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.utilities.Constants;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrganizationDetailFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.org_image_detail) ImageView mOrganizationDetailImage;
    @BindView(R.id.org_name) TextView mOrganizationName;
    @BindView(R.id.org_type) TextView mOrganizationType;
    @BindView(R.id.org_location) TextView mOrganizationLocation;
    @BindView(R.id.phone_fab)
    FloatingActionButton mPhoneFab;
    @BindView(R.id.twitter_fab) FloatingActionButton mTwitterFab;
    @BindView(R.id.facebook_fab) FloatingActionButton mFacebookFab;
    @BindView(R.id.instagram_fab) FloatingActionButton mInstagramFab;
    @BindView(R.id.web_fab) FloatingActionButton mWebFab;
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
    public static final String TAG = OrganizationDetailFragment.class.getSimpleName();
    private DonationDetailsDialogFragment mDonationDetailsFragment;

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
        mPhoneFab.setOnClickListener(this);
        mTwitterFab.setOnClickListener(this);
        mFacebookFab.setOnClickListener(this);
        mInstagramFab.setOnClickListener(this);
        mWebFab.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        if (v == mDonateButton) {
              openDonationDetailsDialog();
              saveToDatabase();
        }

        // Set implicit intents
        if (v == mPhoneFab){
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mRelief.getContacts().getPhone()));
            startActivity(phoneIntent);
        }

        if (v == mTwitterFab){
            Intent twitterIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRelief.getContacts().getTwitter()));
            startActivity(twitterIntent);
        }

        if (v == mFacebookFab){
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRelief.getContacts().getFacebook()));
            startActivity(facebookIntent);
        }

        if (v == mInstagramFab){
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRelief.getContacts().getInstagram()));
            startActivity(instagramIntent);
        }

        if (v == mWebFab){
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRelief.getWebsite()));
            startActivity(webIntent);
        }
    }

    private void saveToDatabase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Objects.requireNonNull(user).getUid();

        String pushId = String.format(Locale.ENGLISH,"relief_%d", mRelief.getId());

        DatabaseReference reliefRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_DONATIONS)
                .child(uid).child(pushId);

        mRelief.setPushId(pushId);

        // Begin interaction
        reliefRef.setValue(mRelief).addOnCompleteListener(requireActivity(), insertTask -> {
            if (insertTask.isSuccessful()){
                Log.d(TAG, "Ongoing donation saved");
            } else {
                Toast.makeText(getContext(), Objects.requireNonNull(insertTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDonationDetailsDialog(){
        // Initialize fragment manager that's responsible for adding, replacing, removing fragments dynamically
        FragmentManager fragmentManager =((AppCompatActivity) requireActivity()).getSupportFragmentManager();
        // Initialize the theme selection fragment
        mDonationDetailsFragment = DonationDetailsDialogFragment.newInstance(mRelief.getContacts().getEmail());
        // Display the theme selection fragment
        mDonationDetailsFragment.show(fragmentManager, "Donation details dialog fragment");
    }
}
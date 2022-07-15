package com.pro.fooddonorke.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.FirebaseOrganizationViewHolder;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.utilities.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationListFragment extends Fragment {
  // declare the variables.
  @BindView(R.id.recyclerviewDonation)
  RecyclerView mRecyclerView;
  @BindView(R.id.progressBarD)
  ProgressBar mProgressBar;
  @BindView(R.id.errorTextViewD)
  TextView mErrorText;
  @BindView(R.id.no_of_charities)
  TextView mNoOfCharities;
  @BindView(R.id.no_of_donations)
  TextView mNoOfDonations;
  @BindView(R.id.loadingTextViewD)
  TextView mLoadingTextView;

  public static final String TAG = DonationListFragment.class.getSimpleName();
  private DatabaseReference mCharityReference;
  private DatabaseReference mDonationStatsReference;
  private FirebaseRecyclerAdapter<Charity, FirebaseOrganizationViewHolder> mFirebaseAdapter;
  private ValueEventListener charityListener;

  public DonationListFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_donation_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    AppCompatActivity activity = (AppCompatActivity) getActivity();

    if (activity != null) {
      Objects.requireNonNull(activity.getSupportActionBar()).setTitle(getString(R.string.donations));
    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = Objects.requireNonNull(user).getUid();


    mCharityReference = FirebaseDatabase.getInstance()
            .getReference(Constants.FIREBASE_CHILD_DONATIONS)
            .child(uid);

    mDonationStatsReference = FirebaseDatabase.getInstance()
                    .getReference(Constants.FIREBASE_CHILD_DONATIONS_STATS)
                    .child(uid).child(Constants.DONATIONS_STAT_FIELD);

    setUpCharityListener();

    setUpFirebaseAdapter();
    hideProgressBar();
    showCharities();
  }

  private void setUpCharityListener(){
    charityListener = new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        mNoOfCharities.setText(String.valueOf(snapshot.getChildrenCount()));
        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(mNoOfCharities);

        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(mNoOfDonations);

        if (!snapshot.hasChildren()){
          showError(getString(R.string.no_donations));
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        Log.d(TAG, "Error while fetching NGOs count: ", error.toException());
      }
    };
  }

  private void setUpFirebaseAdapter() {
    FirebaseRecyclerOptions<Charity> options =
            new FirebaseRecyclerOptions.Builder<Charity>()
                    .setQuery(mCharityReference, Charity.class)
                    .build();

    mFirebaseAdapter = new FirebaseRecyclerAdapter<Charity, FirebaseOrganizationViewHolder>(options) {
      @Override
      protected void onBindViewHolder(@NonNull FirebaseOrganizationViewHolder holder, int position, @NonNull Charity model) {
        holder.bindRelief(model);
      }

      @NonNull
      @Override
      public FirebaseOrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organization_list, parent, false);
        return new FirebaseOrganizationViewHolder(view);
      }
    };

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mFirebaseAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    mFirebaseAdapter.startListening();
    mCharityReference.addValueEventListener(charityListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
    }
    mCharityReference.removeEventListener(charityListener);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mFirebaseAdapter.stopListening();
  }

  private void showCharities() {
    mRecyclerView.setVisibility(View.VISIBLE);
  }

  private void hideProgressBar()  {
    mProgressBar.setVisibility(View.GONE);
    mLoadingTextView.setVisibility(View.GONE);
  }

  private void showError(String message){
    mErrorText.setText(message);
    mErrorText.setVisibility(View.VISIBLE);
  }
}

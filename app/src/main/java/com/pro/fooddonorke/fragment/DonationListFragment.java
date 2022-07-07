package com.pro.fooddonorke.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.FirebaseOrganizationViewHolder;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.ui.SplashScreen;
import com.pro.fooddonorke.utilities.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationListFragment extends Fragment {
//  private FirebaseOrganizationViewHolder mFirebaseAdapter;

  private DatabaseReference mCharityReference;
  private FirebaseRecyclerAdapter<Charity, FirebaseOrganizationViewHolder> mFirebaseAdapter;

  // declare the variables.
  @BindView(R.id.recyclerviewDonation)
  RecyclerView mRecyclerView;
  @BindView(R.id.progressBarD)
  ProgressBar mProgressBar;
  @BindView(R.id.errorTextViewD)
  TextView mErrorText;

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

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = Objects.requireNonNull(user).getUid();


    mCharityReference = FirebaseDatabase.getInstance()
            .getReference(Constants.FIREBASE_CHILD_DONATIONS)
            .child(uid);

    setUpFirebaseAdapter();
    hideProgressBar();
    showCharities();
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
    Log.d("Donation","count" + mFirebaseAdapter.getItemCount());
//        mFireBaseAdapter.startListening();
  }

  @Override
  public void onStart() {
    super.onStart();
        mFirebaseAdapter.startListening();
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
    }
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
  }
}

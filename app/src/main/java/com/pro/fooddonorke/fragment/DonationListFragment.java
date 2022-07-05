package com.pro.fooddonorke.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.FirebaseDonationAdapter;
import com.pro.fooddonorke.ui.SplashScreen;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationListFragment extends Fragment {
  private FirebaseDonationAdapter mFirebaseAdapter;

  // declare the variables.
  @BindView(R.id.recyclerviewDonation)
  RecyclerView recyclerView;
  @BindView(R.id.progressBarD)
  ProgressBar progressBar;
  @BindView(R.id.errorTextViewD)
  TextView errorText;

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
  }

  @Override
  public void onStart() {
    super.onStart();
//        mFirebaseAdapter.startListening();
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mFirebaseAdapter != null){
//            mFirebaseAdapter.stopListening();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
//        mFirebaseAdapter.stopListening();
  }

  private void showMeals() {
    recyclerView.setVisibility(View.VISIBLE);
  }

  private void hideProgressBar() {
    progressBar.setVisibility(View.GONE);
  }
}

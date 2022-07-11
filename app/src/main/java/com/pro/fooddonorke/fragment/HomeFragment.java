package com.pro.fooddonorke.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.DonationRequestAdapter;
import com.pro.fooddonorke.interfaces.ItemOnClickListener;
import com.pro.fooddonorke.models.CharitiesSearchResponse;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.models.CharityDetailResponse;
import com.pro.fooddonorke.models.DonationRequest;
import com.pro.fooddonorke.models.ProfileData;
import com.pro.fooddonorke.models.RequestsSearchResponse;
import com.pro.fooddonorke.network.FoodDonorKeApi;
import com.pro.fooddonorke.network.FoodDonorKeClient;

import java.util.ArrayList;
import java.util.List;

import com.pro.fooddonorke.ui.OrganizationDetailActivity;
import com.pro.fooddonorke.ui.ProfileActivity;
import com.pro.fooddonorke.utilities.Constants;

import org.parceler.Parcels;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ItemOnClickListener {
  @BindView(R.id.listId)
  RecyclerView organization_list;
  @BindView(R.id.profileButtonId)
  Button profileButton;
  @BindView(R.id.welcomeId)
  TextView welcomeText;
  @BindView(R.id.profilePicId)
  ShapeableImageView profilePic;

  private FirebaseAuth auth;
  private DonationRequestAdapter mAdapter;
  private List<DonationRequest> donations;
  private static final String TAG = HomeFragment.class.getSimpleName();
  private FoodDonorKeApi client = FoodDonorKeClient.getClient();
  private ValueEventListener mValueEventListener;
  private DatabaseReference profileDataReference;


  public HomeFragment() {
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    AppCompatActivity activity = (AppCompatActivity) getActivity();

    if (activity != null) {
      Objects.requireNonNull(activity.getSupportActionBar()).setTitle(getString(R.string.home));
    }

    auth  = FirebaseAuth.getInstance();
    profileDataReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PROFILE).child(Objects.requireNonNull(auth.getCurrentUser()).getUid());
    setWelcomeText();
    setUpProfileButton();
    setUpProfileListener();
  }

  private void setUpProfileButton(){
    profileButton.setOnClickListener(view -> {
      Intent intent = new Intent(getContext(), ProfileActivity.class);
      startActivity(intent);
    });
  }

  private void setUpProfileListener(){
    mValueEventListener = new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()){
          ProfileData profileData = snapshot.getValue(ProfileData.class);
          if (profileData != null){
            Glide.with(requireContext()).asBitmap().load(profileData.getImage()).placeholder(R.drawable.profile).into(profilePic);
            loadDonationRequests(profileData.getLocation());
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        Log.d(TAG, "Error fetching profile data", error.toException());
      }
    };
  }

  private void setWelcomeText(){
    FirebaseUser user = auth.getCurrentUser();
    if (user != null){
      welcomeText.setText(getString(R.string.welcome, user.getDisplayName()));
    }
  }

  private void loadDonationRequests(String location) {
    Call<RequestsSearchResponse> call = client.getRequestsByLocation(location);
    call.enqueue(new Callback<RequestsSearchResponse>() {
      @Override
      public void onResponse(@NonNull Call<RequestsSearchResponse> call, @NonNull Response<RequestsSearchResponse> response){
        if (response.isSuccessful()) {
          donations = Objects.requireNonNull(response.body()).getData();
          mAdapter = new DonationRequestAdapter(getActivity(), donations, HomeFragment.this);
          organization_list.setAdapter(mAdapter);
          RecyclerView.LayoutManager layoutManager =
                  new LinearLayoutManager(getActivity());
          organization_list.setLayoutManager(layoutManager);
          organization_list.setHasFixedSize(true);

        //  showDonations();
       // } else {
        //  showUnsuccessfulMessage();
        }
      }
      @Override
      public void onFailure(@NonNull Call<RequestsSearchResponse> call, @NonNull Throwable t) {
        Log.e(TAG, "onFailure: ",t );
      //  hideProgressBar();
      //  showFailureMessage();
      }

    });
  }


  @Override
  public void openCharityDetails(int position) {
    List<Charity> mCharities = new ArrayList<>();
    client.getCharitiesByLocation("Nairobi").enqueue(new Callback<CharitiesSearchResponse>() {
      @Override
      public void onResponse(@NonNull Call<CharitiesSearchResponse> call, @NonNull Response<CharitiesSearchResponse> response) {
        if (response.isSuccessful()){
          ArrayList<Charity> foundCharities = (ArrayList<Charity>) Objects.requireNonNull(response.body()).getData();

          for (DonationRequest request: donations) {
            // Get charity details
            Charity foundCharity = foundCharities.stream()
                    .filter(charity -> charity.getId().equals(request.getCharityId()))
                    .findAny()
                    .orElse(null);
            mCharities.add(foundCharity);
          }

          // Set up intent
          Intent intent = new Intent(getContext(), OrganizationDetailActivity.class);
          intent.putExtra("position", position);
          intent.putExtra("reliefs", Parcels.wrap(mCharities));
          startActivity(intent);

        }
      }

      @Override
      public void onFailure(@NonNull Call<CharitiesSearchResponse> call, @NonNull Throwable t) {
        Log.d(TAG, "Error fetching list of charities", t);
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    profileDataReference.addValueEventListener(mValueEventListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    profileDataReference.removeEventListener(mValueEventListener);
  }
}

package com.pro.fooddonorke.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.DonationAdapter;
import com.pro.fooddonorke.models.DonationRequest;
import com.pro.fooddonorke.models.RequestsSearchResponse;
import com.pro.fooddonorke.network.FoodDonorKeApi;
import com.pro.fooddonorke.network.FoodDonorKeClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
  @BindView(R.id.listId)
  RecyclerView organization_list;
  @BindView(R.id.profileButtonId)
  Button profileButton;
  @BindView(R.id.welcomeId)
  TextView welcomeText;

  private FirebaseAuth auth;
  private DonationAdapter mAdapter;
  private List<DonationRequest> donations;
  private static final String TAG = HomeFragment.class.getSimpleName();

  public HomeFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, view);
    loadRecentDonations("Nairobi");
    return view;

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this,view);

    auth = FirebaseAuth.getInstance();
    setWelcomeText();


  }

  private void setWelcomeText(){
    FirebaseUser user = auth.getCurrentUser();
    if (user != null){
      welcomeText.setText(getString(R.string.welcome, user.getDisplayName()));
    }
  }

  private void loadRecentDonations(String location) {

    FoodDonorKeApi client = FoodDonorKeClient.getClient();

    Call<RequestsSearchResponse> call = client.getRequestsByLocation(location);
    call.enqueue(new Callback<RequestsSearchResponse>() {
      @Override
      public void onResponse(Call<RequestsSearchResponse> call, Response<RequestsSearchResponse> response){
        if (response.isSuccessful()) {
          donations = response.body().getData();
          mAdapter = new DonationAdapter(getActivity(), donations);
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
      public void onFailure(Call<RequestsSearchResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: ",t );
      //  hideProgressBar();
      //  showFailureMessage();
      }

    });
  }
}

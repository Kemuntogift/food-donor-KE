package com.pro.fooddonorke.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.OrganizationListAdapter;
import com.pro.fooddonorke.models.CharitiesSearchResponse;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.network.FoodDonorKeApi;
import com.pro.fooddonorke.network.FoodDonorKeClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationListFragment extends Fragment implements View.OnClickListener{

  public static final String PREFERENCES_LOCATION_KEY = "location";
  private SharedPreferences mSharedPreferences;
  private SharedPreferences.Editor mEditor;
  private String mRecentChoice;
  private static final String TAG = OrganizationListFragment.class.getSimpleName();
  private OrganizationListAdapter mAdapter;

  private List<Charity> reliefs;

  @BindView(R.id.searchText)
  SearchView mSearchText;
  @BindView(R.id.recyclerView)
  RecyclerView mRecyclerView;
  @BindView(R.id.errorTextView)
  TextView mErrorTextView;
  @BindView(R.id.progressBar)
  ProgressBar mProgressBar;


  public OrganizationListFragment() {
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_organization_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    mSearchText.setOnSearchClickListener(this);

    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    mRecentChoice = mSharedPreferences.getString(PREFERENCES_LOCATION_KEY, null);
    if(mRecentChoice != null){
      fetchCharities(mRecentChoice);
    }
  }
  @Override
  public void onClick(View v){
    if (v == mSearchText) {
      String location = mSearchText.getTransitionName();
      if(!(location).equals("")) {
        addToSharedPreferences(location);
      }
    }
  }
  private void addToSharedPreferences(String location) {
    mEditor.putString(PREFERENCES_LOCATION_KEY, location).apply();
  }


  private void showFailureMessage() {
    mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
    mErrorTextView.setVisibility(View.VISIBLE);
  }

  private void showUnsuccessfulMessage() {
    mErrorTextView.setText("Something went wrong. Please try again later");
    mErrorTextView.setVisibility(View.VISIBLE);
  }

  private void showCharities() {
    mRecyclerView.setVisibility(View.VISIBLE);
  }

  private void hideProgressBar() {
    mProgressBar.setVisibility(View.GONE);
  }



  private void fetchCharities(String location){
    FoodDonorKeApi client = FoodDonorKeClient.getClient();

    Call<CharitiesSearchResponse> call = client.getCharitiesByLocation(location);

    call.enqueue(new Callback<CharitiesSearchResponse>() {
      @Override
      public void onResponse(Call<CharitiesSearchResponse> call, Response<CharitiesSearchResponse> response) {
        hideProgressBar();

        if (response.isSuccessful()) {
          reliefs = response.body().getData();
          mAdapter = new OrganizationListAdapter(OrganizationListFragment.this, reliefs);
          mRecyclerView.setAdapter(mAdapter);
          RecyclerView.LayoutManager layoutManager =
                  new LinearLayoutManager(getContext());
          mRecyclerView.setLayoutManager(layoutManager);
          mRecyclerView.setHasFixedSize(true);
          showCharities();
        } else {
          showUnsuccessfulMessage();
        }
      }

      @Override
      public void onFailure(Call<CharitiesSearchResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: ",t );
        hideProgressBar();
        showFailureMessage();
      }

    });
  }
}

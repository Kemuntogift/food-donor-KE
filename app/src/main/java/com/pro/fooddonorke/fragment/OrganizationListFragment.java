package com.pro.fooddonorke.fragment;


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
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationListFragment extends Fragment {
  private static final String TAG = OrganizationListFragment.class.getSimpleName();
  public static final String PREFERENCES_LOCATION_KEY = "location";
  private SharedPreferences mSharedPreferences;
  private OrganizationListAdapter mAdapter;
  private SharedPreferences.Editor mEditor;
  private String mRecentChoice;


  private List<Charity> reliefs;

  @BindView(R.id.searchText)
  SearchView mSearchText;
  @BindView(R.id.recyclerView)
  RecyclerView mRecyclerView;
  @BindView(R.id.errorTextView)
  TextView mErrorTextView;
  @BindView(R.id.progressBar)
  ProgressBar mProgressBar;
  @BindView(R.id.loading_text_view)
  TextView mLoadingTextView;


  public OrganizationListFragment() {
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view =  inflater.inflate(R.layout.fragment_organization_list, container, false);
    ButterKnife.bind(this, view);


    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    AppCompatActivity activity = (AppCompatActivity) getActivity();

    if (activity != null) {
      Objects.requireNonNull(activity.getSupportActionBar()).setTitle(getString(R.string.charities));
    }

    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    mRecentChoice = mSharedPreferences.getString(PREFERENCES_LOCATION_KEY, null);
    if(mRecentChoice != null){
      fetchCharities(mRecentChoice);
    }
    setUpSearchView();
  }

  private void setUpSearchView(){
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    mEditor = mSharedPreferences.edit();

    mSearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String location) {
        addToSharedPreferences(location);
        fetchCharities(location);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String location) {
        return false;
      }
    });
  }

  private void addToSharedPreferences(String location) {
    mEditor.putString(PREFERENCES_LOCATION_KEY, location).apply();
  }


  private void showFailureMessage() {
    mErrorTextView.setText(getString(R.string.failure_message));
    mErrorTextView.setVisibility(View.VISIBLE);
  }

  private void showUnsuccessfulMessage() {
    mErrorTextView.setText(getString(R.string.unsuccessful_message));
    mErrorTextView.setVisibility(View.VISIBLE);
  }

  private void showCharities() {
    mRecyclerView.setVisibility(View.VISIBLE);
  }

  private void hideProgressBar() {
    mProgressBar.setVisibility(View.GONE);
    mLoadingTextView.setVisibility(View.GONE);
  }



  private void fetchCharities(String location){
    FoodDonorKeApi client = FoodDonorKeClient.getClient();

    Call<CharitiesSearchResponse> call = client.getCharitiesByLocation(location);

    call.enqueue(new Callback<CharitiesSearchResponse>() {
      @Override
      public void onResponse(@NonNull Call<CharitiesSearchResponse> call, @NonNull Response<CharitiesSearchResponse> response) {
        hideProgressBar();

        if (response.isSuccessful()) {
          reliefs = Objects.requireNonNull(response.body()).getData();
          RecyclerView.LayoutManager layoutManager =
                  new LinearLayoutManager(getContext());
          mRecyclerView.setLayoutManager(layoutManager);
          mAdapter = new OrganizationListAdapter(getContext(), reliefs);
          mRecyclerView.setAdapter(mAdapter);
          mRecyclerView.setHasFixedSize(true);
          showCharities();
          Log.d(TAG, "Count: " + mAdapter.getItemCount());
        } else {
          showUnsuccessfulMessage();
        }
      }

      @Override
      public void onFailure(@NonNull Call<CharitiesSearchResponse> call, @NonNull Throwable t) {
        Log.e(TAG, "onFailure: ",t );
        hideProgressBar();
        showFailureMessage();
      }
    });
  }
}

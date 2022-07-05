package com.pro.fooddonorke.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.OrganizationListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrganizationListFragment extends Fragment {
  private List<String> charities;
  @BindView(R.id.searchText)
  SearchView mSearchText;
  @BindView(R.id.recyclerView)
  RecyclerView mRecyclerView;
  @BindView(R.id.errorTextView)
  TextView mErrorTextView;
  @BindView(R.id.progressBar)
  ProgressBar mProgressBar;

  private OrganizationListAdapter mAdapter;

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

  }
}

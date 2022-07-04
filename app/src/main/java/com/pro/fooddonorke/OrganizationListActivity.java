package com.pro.fooddonorke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrganizationListActivity extends AppCompatActivity {
    private List<String> charities;
    @BindView(R.id.searchImage)
    ImageView mSearchView;
    @BindView(R.id.searchText)
    EditText mSearchText;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.errorTextView)
    TextView mErrorTextView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private OrganizationListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String organization = intent.getStringExtra("organization");
    }
}
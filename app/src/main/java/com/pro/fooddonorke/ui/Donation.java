package com.pro.fooddonorke.ui;

import static com.pro.fooddonorke.utilities.Constants.FIREBASE_CHILD_DONATIONS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.adapters.FirebaseOrganizationViewHolder;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.utilities.Constants;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Donation extends AppCompatActivity {

    private DatabaseReference mCharityReference;
    private FirebaseRecyclerAdapter<Charity, FirebaseOrganizationViewHolder> mFireBaseAdapter;

    // declare the variables.
    @BindView(R.id.recyclerviewDonation) RecyclerView mRecyclerView;
    @BindView(R.id.progressBarD) ProgressBar mProgressBar;
    @BindView(R.id.errorTextViewD) TextView mErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        ButterKnife.bind(this);


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

        mFireBaseAdapter = new FirebaseRecyclerAdapter<Charity, FirebaseOrganizationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseOrganizationViewHolder holder, int position, @NonNull Charity model) {
                holder.bindRelief(model);
            }

            @NonNull
            @Override
            public FirebaseOrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_donation_list, parent, false);
                return new FirebaseOrganizationViewHolder(view);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFireBaseAdapter.startListening();
    }

    // populating the menu and logging out a user.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_out){
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFireBaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFireBaseAdapter != null){
            mFireBaseAdapter.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFireBaseAdapter.stopListening();
    }

    private void showCharities() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar()  {
        mProgressBar.setVisibility(View.GONE);
    }
}
package com.pro.fooddonorke.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.ui.HomeActivity;
import com.pro.fooddonorke.ui.ProfileActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
  @BindView(R.id.listId)
  RecyclerView organization_list;
  @BindView(R.id.profileButtonId)
  Button profileButton;
  @BindView(R.id.welcomeId)
  TextView welcomeText;

  private FirebaseAuth auth;

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
    ButterKnife.bind(this,view);

    AppCompatActivity activity = (AppCompatActivity) getActivity();

    if (activity != null) {
      Objects.requireNonNull(activity.getSupportActionBar()).setTitle(getString(R.string.home));
    }

    auth = FirebaseAuth.getInstance();
    setWelcomeText();
    setUpProfileButton();
  }

  private void setUpProfileButton(){
    profileButton.setOnClickListener(view -> {
      Intent intent = new Intent(getContext(), ProfileActivity.class);
      startActivity(intent);
    });
  }

  private void setWelcomeText(){
    FirebaseUser user = auth.getCurrentUser();
    if (user != null){
      welcomeText.setText(getString(R.string.welcome, user.getDisplayName()));
    }
  }
}

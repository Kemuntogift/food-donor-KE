package com.pro.fooddonorke;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView (R.id.listId)
    RecyclerView organization_list;
    @BindView(R.id.profileButtonId)
    Button profileButton;
    @BindView(R.id.welcomeId)
    TextView welcomeText;
    @BindView(R.id.logoutId)
    Button logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

    }
}
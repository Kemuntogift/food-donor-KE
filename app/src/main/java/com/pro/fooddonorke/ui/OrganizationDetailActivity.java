package com.pro.fooddonorke.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.pro.fooddonorke.R;
import com.pro.fooddonorke.models.Charity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrganizationDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private com.pro.fooddonorke.adapters.OrganizationPagerAdapter adapterViewPager;
    List<Charity> mReliefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_detail);
        ButterKnife.bind(this);

        mReliefs = Parcels.unwrap(getIntent().getParcelableExtra("reliefs"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new com.pro.fooddonorke.adapters.OrganizationPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mReliefs);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}
package com.pro.fooddonorke.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pro.fooddonorke.fragment.OrganizationDetailFragment;
import com.pro.fooddonorke.models.Charity;

import java.util.List;

public class OrganizationPagerAdapter extends FragmentPagerAdapter {
    private List<Charity> mReliefs;

    public OrganizationPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Charity> reliefs) {
        super(fm, behavior);
        mReliefs = reliefs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return OrganizationDetailFragment.newInstance(mReliefs.get(position));
    }

    @Override
    public int getCount() {
        return mReliefs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mReliefs.get(position).getName();
    }
}

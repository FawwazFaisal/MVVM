package com.omnisoft.retrofitpractice.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;
    ArrayList<String> titles;

    public FragmentAdapter(@NonNull FragmentManager fm, int mode) {
        super(fm, mode);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void addFragments(Fragment frag, String title) {
        fragments.add(frag);
        titles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

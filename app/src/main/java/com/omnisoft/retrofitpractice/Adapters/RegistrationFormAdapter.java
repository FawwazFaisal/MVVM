package com.omnisoft.retrofitpractice.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by S.M.Mubbashir.A.Z. on 3/26/2021.
 */
public class RegistrationFormAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragList;
    ArrayList<String> titleList;

    public RegistrationFormAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(String title, Fragment fragment) {
        titleList.add(title);
        fragList.add(fragment);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}

package com.omnisoft.retrofitpractice.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.omnisoft.retrofitpractice.Adapters.FragmentAdapter;
import com.omnisoft.retrofitpractice.Fragments.AdMobFragment;
import com.omnisoft.retrofitpractice.Fragments.HeroesFragment;
import com.omnisoft.retrofitpractice.Fragments.MapsFragment;
import com.omnisoft.retrofitpractice.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    ActivityMainBinding bd;

    //Attach observer by passing context AND Observer Callback
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
//        goToMenuActivity();
        //setFragment();
        setUpFragmentAdapterAndViewPager();
    }

    private void goToMenuActivity() {
        finish();
        startActivity(new Intent(this, MenuActivity.class));
    }

    void setUpFragmentAdapterAndViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragments(new HeroesFragment(), "MVVM");
        adapter.addFragments(new MapsFragment(), "maps");
        adapter.addFragments(new AdMobFragment(), "ad");
        bd.viewPager.setAdapter(adapter);
        bd.tabLayout.setupWithViewPager(bd.viewPager);
        bd.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        if (!checkPermission()) {
                            askPermission(false);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(bd.viewPager.getId(), new HeroesFragment());
        transaction.commit();
    }
}
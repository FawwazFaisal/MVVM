package com.omnisoft.retrofitpractice.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.omnisoft.retrofitpractice.Adapters.FragmentAdapter;
import com.omnisoft.retrofitpractice.Adapters.MenuAdapter;
import com.omnisoft.retrofitpractice.App;
import com.omnisoft.retrofitpractice.CustomViews.DuoMenuView;
import com.omnisoft.retrofitpractice.Fragments.AdMobFragment;
import com.omnisoft.retrofitpractice.Fragments.HeroesFragment;
import com.omnisoft.retrofitpractice.Fragments.MapsFragment;
import com.omnisoft.retrofitpractice.R;
import com.omnisoft.retrofitpractice.Utility.SharedPreferences;
import com.omnisoft.retrofitpractice.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements DuoMenuView.OnMenuClickListener, View.OnClickListener {

    ActivityMainBinding bd;
    ArrayList<String> mOptionTitles = new ArrayList<>();
    ArrayList<Drawable> mOptionIcons = new ArrayList<>();
    View mFooter;
    View mHeader;
    int mCurrentItem = 0;

    //Attach observer by passing context AND Observer Callback
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        //setFragment();
        setUpFragmentAdapterAndViewPager();
        handleMenu();
        setPermitDialog();
    }

    private void handleMenu() {
        setOptionTitles();
        setOptionIcons();
        MenuAdapter adapter = new MenuAdapter(mOptionTitles, mOptionIcons);
        bd.menu.setAdapter(adapter);
        mFooter = bd.menu.getFooterView();
        mFooter.findViewById(R.id.logout).setOnClickListener(this);
        mHeader = bd.menu.getHeaderView();
        bd.menu.setSelected(true);
        bd.menu.setOnMenuClickListener(this);
        bd.content.menuBtn.setOnClickListener(this);
    }

    private void setOptionIcons() {
        mOptionTitles.add("MVVM");
        mOptionTitles.add("Maps");
        mOptionTitles.add("Ads");
    }

    private void setOptionTitles() {
        mOptionIcons.add(ContextCompat.getDrawable(this, R.drawable.ic_arrow_right_24));
        mOptionIcons.add(ContextCompat.getDrawable(this, R.drawable.ic_arrow_right_24));
        mOptionIcons.add(ContextCompat.getDrawable(this, R.drawable.ic_arrow_right_24));
    }

    void setUpFragmentAdapterAndViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragments(new HeroesFragment(), "MVVM");
        adapter.addFragments(new MapsFragment(), "MAPS");
        adapter.addFragments(new AdMobFragment(), "AD");
        bd.content.viewPager.setAdapter(adapter);
        bd.content.tabLayout.setupWithViewPager(bd.content.viewPager);
        bd.content.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        transaction.replace(bd.content.viewPager.getId(), new HeroesFragment());
        transaction.commit();
    }

    @Override
    public void onFooterClicked() {
        toggleMenu();
        SharedPreferences.getPrefs().edit().clear().apply();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onHeaderClicked() {
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        toggleMenu();
        if (mCurrentItem != position) {
            bd.content.viewPager.setCurrentItem(position);
        }
        mCurrentItem = position;
    }

    private void toggleMenu() {
        if (bd.root.isDrawerOpen()) {
            bd.root.closeDrawer();
        } else {
            bd.root.openDrawer();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == bd.content.menuBtn.getId()) {
            toggleMenu();
        }
        if (v.getId() == R.id.logout) {
            FirebaseMessaging.getInstance().setAutoInitEnabled(false);
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseFirestore.getInstance().collection("user").document(App.user.email).update("FCMToken", "").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    App.user = null;
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
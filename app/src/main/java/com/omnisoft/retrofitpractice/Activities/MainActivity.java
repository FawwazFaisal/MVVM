package com.omnisoft.retrofitpractice.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.omnisoft.retrofitpractice.Adapters.FragmentAdapter;
import com.omnisoft.retrofitpractice.App;
import com.omnisoft.retrofitpractice.Fragments.AdMobFragment;
import com.omnisoft.retrofitpractice.Fragments.HeroesFragment;
import com.omnisoft.retrofitpractice.Fragments.MapsFragment;
import com.omnisoft.retrofitpractice.Utility.BroadcastRegistry;
import com.omnisoft.retrofitpractice.Utility.CustomizeUI;
import com.omnisoft.retrofitpractice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bd;
    BroadcastRegistry receiver;
    boolean isReceiverRegistered = false;


    //Attach observer by passing context AND Observer Callback
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setContext(this);
        bd = ActivityMainBinding.inflate(getLayoutInflater());
        CustomizeUI.setFullscreen(this);
        setContentView(bd.getRoot());
        //setFragment();
        setUpFragmentAdapterAndViewPager();
    }

    private void checkGPSState() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        } else {
            isReceiverRegistered = true;
            registerReceiver(receiver, new IntentFilter(
                    ConnectivityManager.CONNECTIVITY_ACTION));
            registerReceiver(receiver, new IntentFilter(
                    LocationManager.PROVIDERS_CHANGED_ACTION));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BroadcastRegistry(this);
        checkGPSState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null && isReceiverRegistered)
            unregisterReceiver(receiver);
    }

    void setUpFragmentAdapterAndViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragments(new HeroesFragment(), "heroes");
        adapter.addFragments(new MapsFragment(), "maps");
        adapter.addFragments(new AdMobFragment(), "ad");
        bd.viewPager.setAdapter(adapter);
        bd.tabLayout.setupWithViewPager(bd.viewPager);
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(bd.viewPager.getId(), new HeroesFragment());
        transaction.commit();
    }

}
package com.omnisoft.retrofitpractice.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.omnisoft.retrofitpractice.Adapters.FragmentAdapter;
import com.omnisoft.retrofitpractice.App;
import com.omnisoft.retrofitpractice.Fragments.AdMobFragment;
import com.omnisoft.retrofitpractice.Fragments.HeroesFragment;
import com.omnisoft.retrofitpractice.Fragments.MapsFragment;
import com.omnisoft.retrofitpractice.R;
import com.omnisoft.retrofitpractice.Utility.BroadcastRegistry;
import com.omnisoft.retrofitpractice.Utility.CustomizeUI;
import com.omnisoft.retrofitpractice.databinding.ActivityMainBinding;

import static com.omnisoft.retrofitpractice.Utility.Constants.COARSE_LOCATION;
import static com.omnisoft.retrofitpractice.Utility.Constants.FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bd;
    BroadcastRegistry receiver;
    boolean isReceiverRegistered = false;
    Dialog permissionDialog;

    //Attach observer by passing context AND Observer Callback
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setContext(this);
        bd = ActivityMainBinding.inflate(getLayoutInflater());
        CustomizeUI.setFullscreen(this);
        setContentView(bd.getRoot());
        //setFragment();
        setPermitDialog();
        setUpFragmentAdapterAndViewPager();
    }

    private void setPermitDialog() {
        permissionDialog = new Dialog(this);
        View view = View.inflate(this, R.layout.permission_dialog, null);
        permissionDialog.setContentView(view);
    }

    public boolean checkGPSState() {
        boolean isEnabled = false;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        } else {
            isEnabled = true;
        }
        return isEnabled;
    }

    private void setReceiver() {
        isReceiverRegistered = true;
        registerReceiver(receiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(receiver, new IntentFilter(
                LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    public void showEnableGPSDialog(int requestCode) {
        Button yesBtn = permissionDialog.getWindow().getDecorView().findViewById(com.omnisoft.retrofitpractice.R.id.yes);
        Button noBtn = permissionDialog.getWindow().getDecorView().findViewById(com.omnisoft.retrofitpractice.R.id.no);
        yesBtn.setOnClickListener(v -> {
            permissionDialog.dismiss();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), requestCode);
        });
        noBtn.setOnClickListener(v -> {
            permissionDialog.dismiss();
        });
        permissionDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        permissionDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else if (requestCode == 1) {
            if (!checkGPSState()) {
                showEnableGPSDialog(0);
            } else {
                permissionDialog.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BroadcastRegistry(this);
        setReceiver();
        if (!checkPermission()) {
            checkGPSState();
        }
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

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermission(boolean forceAsk) {
        if (forceAsk) {
            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", this.getPackageName(), null)));
        } else {
            requestPermissions(new String[]{COARSE_LOCATION, FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean neverAskAgain = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) && !shouldShowRequestPermissionRationale(FINE_LOCATION);
            if (neverAskAgain) {
                Toast.makeText(this, "Go to App settings and enable Location Permission", Toast.LENGTH_SHORT).show();
                askPermission(true);
            }
        }
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(bd.viewPager.getId(), new HeroesFragment());
        transaction.commit();
    }

}
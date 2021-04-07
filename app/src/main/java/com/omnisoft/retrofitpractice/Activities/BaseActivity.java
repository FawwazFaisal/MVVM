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

import com.omnisoft.retrofitpractice.App;
import com.omnisoft.retrofitpractice.R;
import com.omnisoft.retrofitpractice.Utility.BroadcastRegistry;
import com.omnisoft.retrofitpractice.Utility.CustomizeUI;

import static com.omnisoft.retrofitpractice.Utility.Constants.COARSE_LOCATION;
import static com.omnisoft.retrofitpractice.Utility.Constants.FINE_LOCATION;

public class BaseActivity extends AppCompatActivity {
    BroadcastRegistry receiver;
    Dialog permissionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomizeUI.setFullscreen(this);
        App.setContext(this);
        setPermitDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null)
            unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setReceiver();
        if (checkPermission()) {
            if (!checkGPSState()) {
                showEnableGPSDialog(0);
            }
        }
    }

    private void setReceiver() {
        receiver = new BroadcastRegistry(this);
        registerReceiver(receiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(receiver, new IntentFilter(
                LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    public void setPermitDialog() {
        permissionDialog = new Dialog(this);
        View view = View.inflate(this, R.layout.permission_dialog, null);
        permissionDialog.setContentView(view);
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkGPSState() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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

    public void askPermission(boolean forceAsk) {
        if (forceAsk) {
            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", this.getPackageName(), null)));
        } else {
            requestPermissions(new String[]{COARSE_LOCATION, FINE_LOCATION}, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            onResume();
        } else if (requestCode == 1) {
            if (!checkGPSState()) {
                showEnableGPSDialog(0);
            }
        }
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
}
package com.omnisoft.retrofitpractice.Utility;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.omnisoft.retrofitpractice.Activities.MainActivity;

public class BroadcastRegistry extends BroadcastReceiver {

    Activity activity;
    ConnectivityManager connectivityManager;
    LocationManager locationManager;

    public BroadcastRegistry() {

    }

    public BroadcastRegistry(Activity activity) {
        this.activity = activity;
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//        NetworkRequest request = new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED).build();
//        connectivityManager.registerNetworkCallback(request,new NetStateListener());
    }

    boolean isOnline() {
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
    }

    boolean isGPSAvailable() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            new Handler().postDelayed(() -> {
                if (isOnline()) {
                    Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }, 1000);
        } else if (intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
            if (!isGPSAvailable()) {
                activity.finish();
                activity.startActivity(new Intent(context, MainActivity.class));
            } else {
                Toast.makeText(context, "enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class NetStateListener extends NetworkCallback {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            if (!isOnline()) {
                Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.omnisoft.retrofitpractice.Utility;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.omnisoft.retrofitpractice.App;

public class LiveLocation extends LiveData<Location> {

    LocationRequest request;
    LocationCallback locCallback;
    FusedLocationProviderClient locationProviderClient;

    @SuppressLint("MissingPermission")
    public LiveLocation() {
        super();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(App.getContext());
        request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);
        request.setFastestInterval(500);

        locCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    setValue(locationResult.getLastLocation());
                }
            }
        };
    }

    @Override
    protected void postValue(Location value) {
        super.postValue(value);
    }

    @Override
    protected void setValue(Location value) {
        super.setValue(value);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        locationProviderClient.removeLocationUpdates(locCallback);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActive() {
        super.onActive();
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                setValue(location);
            }
        });
        locationProviderClient.requestLocationUpdates(request, locCallback, Looper.getMainLooper());
    }
}

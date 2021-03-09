package com.omnisoft.retrofitpractice.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.omnisoft.retrofitpractice.R;
import com.omnisoft.retrofitpractice.Utility.LiveLocation;
import com.omnisoft.retrofitpractice.databinding.FragmentMapsBinding;

import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    FragmentMapsBinding bd;
    String fineLocPermit = Manifest.permission.ACCESS_FINE_LOCATION;
    String coarseLocPermit = Manifest.permission.ACCESS_COARSE_LOCATION;
    LiveLocation mLocation;
    MarkerOptions marker = new MarkerOptions();
    GoogleMap map;
    boolean hasMapAnimated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bd = FragmentMapsBinding.inflate(inflater, container, false);
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setList();
        if (!checkPermission()) {
            askPermission(false);
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), fineLocPermit) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), coarseLocPermit) == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermission(boolean forceAsk) {
        if (forceAsk) {
            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", requireContext().getPackageName(), null)));
        } else {
            requestPermissions(new String[]{coarseLocPermit, fineLocPermit}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && this.isVisible()) {
            boolean neverAskAgain = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) && !shouldShowRequestPermissionRationale(fineLocPermit);
            if (neverAskAgain) {
                Toast.makeText(requireContext(), "Got to App permission and enable Location Permission", Toast.LENGTH_SHORT).show();
                askPermission(true);
            }
        }
    }

    private void setLocationListener() {
        mLocation = new LiveLocation();
        setMaps();
        mLocation.observe(this, location -> {
            if (!hasMapAnimated) {
                hasMapAnimated = true;
                animateMap();
            }
            if (location != null) {
                setMarker();
            }
        });
    }

    private void setList() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.locations));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.spinner_dropdown, list);
        bd.spinner.setAdapter(adapter);
    }

    private void setMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    void animateMap() {
        LatLng currentLoc = new LatLng(mLocation.getValue().getLatitude(), mLocation.getValue().getLongitude());
        map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().zoom(18f).target(currentLoc).build()));
    }

    void setMarker() {
        marker.position(new LatLng(mLocation.getValue().getLatitude(), mLocation.getValue().getLongitude())).title("Marker");
        map.clear();
        map.addMarker(marker);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermission()) {
            setLocationListener();
        } else {
            askPermission(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocation.removeObservers(this);
    }
}
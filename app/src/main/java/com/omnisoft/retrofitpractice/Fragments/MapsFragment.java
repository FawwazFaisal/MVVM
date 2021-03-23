package com.omnisoft.retrofitpractice.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.omnisoft.retrofitpractice.Activities.MainActivity;
import com.omnisoft.retrofitpractice.R;
import com.omnisoft.retrofitpractice.Utility.LiveLocation;
import com.omnisoft.retrofitpractice.databinding.FragmentMapsBinding;

import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    FragmentMapsBinding bd;
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

    @Override
    public void onResume() {
        super.onResume();
        if (((MainActivity) requireActivity()).checkPermission()) {
            if (((MainActivity) requireActivity()).checkGPSState()) {
                setLocationListener();
            } else {
                ((MainActivity) requireActivity()).showEnableGPSDialog(0);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocation != null) {
            mLocation.removeObservers(this);
        }
    }
}
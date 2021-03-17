package com.omnisoft.retrofitpractice.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.omnisoft.retrofitpractice.R;

public class AdMobFragment extends Fragment {
    // Remove the below line after defining your own ad unit ID.

    private static final int START_LEVEL = 1;
    private int mLevel;
    private Button mNextLevelButton;
    private InterstitialAd mInterstitialAd;
    private TextView mLevelTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ad_mob, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the next level button, which tries to show an interstitial when clicked.
        mNextLevelButton = view.findViewById(R.id.next_level_button);

        // Create the text view to show the level number.
        mLevelTextView = view.findViewById(R.id.level);
        mLevel = START_LEVEL;
//        List<String> testDeviceIds = Collections.singletonList("E524BDE5E2FDDF478D9A8D9CD0DF2EFA");
//        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        MobileAds.setRequestConfiguration(configuration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getActivity().getApplicationContext() == null) return;
        final Context appContext = getActivity().getApplicationContext();

        mNextLevelButton.setEnabled(false);
        mNextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial(appContext);
            }
        });
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd(appContext);
        loadInterstitial();
    }

    private InterstitialAd newInterstitialAd(final Context context) {
        InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(context, "Ad Loaded", Toast.LENGTH_SHORT).show();
                mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(context, "Ad Error", Toast.LENGTH_SHORT).show();
                mNextLevelButton.setEnabled(false);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                Toast.makeText(context, "Ad Closed", Toast.LENGTH_SHORT).show();
                goToNextLevel(context);
            }
        });
        return interstitialAd;
    }

    private void showInterstitial(Context context) {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(context, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        mNextLevelButton.setEnabled(false);
        AdRequest adRequest = new AdRequest.Builder().build();
//        AdRequest adRequest = new AdRequest.Builder()
//                .setRequestAgent("android_studio:ad_template").build();
//        Toast.makeText(requireContext(), String.valueOf(adRequest.isTestDevice(requireContext())), Toast.LENGTH_SHORT).show();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel(Context context) {
        // Show the next level and reload the ad to prepare for the level after.
        mLevelTextView.setText(context.getString(R.string.level_text, ++mLevel));
        mInterstitialAd = newInterstitialAd(context);
        loadInterstitial();
    }
}
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.omnisoft.retrofitpractice.R;

import java.util.Collections;
import java.util.List;

public class AdMobFragment extends Fragment {
    // Remove the below line after defining your own ad unit ID.

    private static final int START_LEVEL = 1;
    public static AdRequest adRequest;
    private static InterstitialAd mInterstitialAd;
    private int mLevel;
    private Button mNextLevelButton;
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
        mNextLevelButton.setEnabled(true);
        mNextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAdMob();
            }
        });
        // Create the text view to show the level number.
        mLevelTextView = view.findViewById(R.id.level);
        mLevel = START_LEVEL;
        List<String> testDeviceIds = Collections.singletonList("E524BDE5E2FDDF478D9A8D9CD0DF2EFA");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        RequestConfiguration configuration = new RequestConfiguration.Builder().build();
        MobileAds.setRequestConfiguration(configuration);
    }

    public void loadAdMob() {
        adRequest = new AdRequest.Builder().build();
        Toast.makeText(requireContext(), String.valueOf(adRequest.isTestDevice(requireContext())), Toast.LENGTH_SHORT).show();
        InterstitialAd.load(requireContext(), getString(R.string.interstitial_ad_unit_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mNextLevelButton.setEnabled(false);
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                showInterstitial(requireContext());
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Toast.makeText(requireContext(), loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                mNextLevelButton.setEnabled(false);
                mInterstitialAd = null;
            }
        });
    }

    private void showInterstitial(Context context) {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    goToNextLevel(requireContext());
                    //// perform your code that you wants todo after ad dismissed or closed
                }

                @Override
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    mInterstitialAd = null;
                    Toast.makeText(requireContext(), adError.getMessage(), Toast.LENGTH_SHORT).show();
                    /// perform your action here when ad will not load
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Toast.makeText(requireContext(), "Ad shown successfully", Toast.LENGTH_SHORT).show();
                    mInterstitialAd = null;

                }
            });
        } else {
            Toast.makeText(context, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }
    private void goToNextLevel(Context context) {
        // Show the next level and reload the ad to prepare for the level after.
        mLevelTextView.setText(context.getString(R.string.level_text, ++mLevel));
        mNextLevelButton.setEnabled(true);
    }
}
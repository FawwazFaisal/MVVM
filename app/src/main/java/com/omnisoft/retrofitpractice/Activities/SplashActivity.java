package com.omnisoft.retrofitpractice.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.github.florent37.viewanimator.ViewAnimator;
import com.omnisoft.retrofitpractice.Utility.CustomizeUI;
import com.omnisoft.retrofitpractice.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomizeUI.setSplashScreenAtr(this);
        bd = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setAnimation();
    }

    private void setAnimation() {
        ViewAnimator animator = ViewAnimator.animate(bd.splashLogo).fadeIn()
                .andAnimate(bd.splashTitle).fadeIn().duration(1000).startDelay(200)
                .thenAnimate(bd.splashLogo).rotation(360).duration(1000).startDelay(1000)
                .thenAnimate(bd.splashLogo).fadeOut().duration(1000).accelerate().startDelay(1000);
        animator.onStop(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        animator.start();
    }
}
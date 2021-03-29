package com.omnisoft.retrofitpractice.Utility.Snack;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class CustomSnack {
    private static Snackbar snackbar;

    public static void showSnackbar(Activity activity, String msg, String btn) {
        View decorView = activity.getWindow().getDecorView().getRootView().getRootView();
        decorView.setPadding(0, 0, 0, 50);
        snackbar = Snackbar.make(decorView, msg, Snackbar.LENGTH_SHORT)
                .setAction(btn, v -> dismiss())
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setBackgroundTint(Color.parseColor("#010101"));

        snackbar.show();
    }

    private static void dismiss() {
        snackbar.dismiss();
    }
}

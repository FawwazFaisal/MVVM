package com.omnisoft.retrofitpractice.Utility.Snack;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.material.snackbar.Snackbar;

public class CustomSnack {
    private static Snackbar snackbar;

    public static void showSnackbar(Activity activity, String msg, String btn) {
        snackbar = Snackbar.make(activity.getWindow().getDecorView().getRootView().getRootView(), msg, Snackbar.LENGTH_SHORT)
                .setAction(btn, v -> dismiss())
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setBackgroundTint(Color.parseColor("#010101"));

        snackbar.show();
    }

    private static void dismiss() {
        snackbar.dismiss();
    }
}

package com.omnisoft.retrofitpractice.Utility.Snack;

import android.app.Activity;
import android.graphics.Color;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.omnisoft.retrofitpractice.R;

public class CustomSnack {
    private static Snackbar snackbar;
    public static void showSnackbar(Activity activity, String msg, String btn) {
        snackbar = Snackbar.make(activity.getWindow().getDecorView().findViewById(R.id.root), msg, Snackbar.LENGTH_SHORT)
                .setAction(btn, v -> dismiss())
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setBackgroundTint(Color.parseColor("#010101"));

        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();

//        params.setMargins(20,0,20,48);
//        snackBarView.getChildAt(0).setY(-10);
//        snackBarView.getChildAt(0).setLayoutParams(params);
//        snackBarView.setY(-100);
//        snackBarView.getChildAt(0).setLayoutParams(params);
//        snackbar.getView().setBackground(ContextCompat.getDrawable(activity,R.drawable.snackbar_bg));
        snackbar.show();
    }

    private static void dismiss() {
        snackbar.dismiss();
    }
}

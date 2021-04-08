package com.omnisoft.retrofitpractice.Utility;

import android.content.Context;

import com.omnisoft.retrofitpractice.App;

/**
 * Created by S.M.Mubbashir.A.Z. on 3/25/2021.
 */
public class SharedPreferences {
    public static android.content.SharedPreferences getPrefs() {
        return App.context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }
}

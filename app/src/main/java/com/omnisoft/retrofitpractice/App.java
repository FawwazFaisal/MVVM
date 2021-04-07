package com.omnisoft.retrofitpractice;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.omnisoft.retrofitpractice.Room.User;

public class App extends Application {
    private static Context context;
    public static User user;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        App.context = context;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}

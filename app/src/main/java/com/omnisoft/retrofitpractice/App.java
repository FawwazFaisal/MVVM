package com.omnisoft.retrofitpractice;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.omnisoft.retrofitpractice.Room.User;

public class App extends Application {
    public static Context context;
    private static User user;


    public static void hideKeyboard(Activity activity) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setContext(Context context) {
        App.context = context;
    }

    public static User getUser() {
        if (user == null) {
            user = new User("");
        }
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }

    public static void removeUser() {
        user = null;
    }
}
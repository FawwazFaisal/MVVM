package com.omnisoft.retrofitpractice.Utility;

import android.app.Activity;
import android.view.View;

public class CustomizeUI {

    public static void setFullscreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

    }
}

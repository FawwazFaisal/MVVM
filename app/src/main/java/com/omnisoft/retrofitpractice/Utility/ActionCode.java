package com.omnisoft.retrofitpractice.Utility;

import com.google.firebase.auth.ActionCodeSettings;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/9/2021.
 */
public class ActionCode {
    public ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            .setUrl("https://www.example.com/finishSignUp")
            // This must be true
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.example.ios")
            .setAndroidPackageName(
                    "com.example.android",
                    true, /* installIfNotAvailable */
                    "12"    /* minimumVersion */)
            .build();
}

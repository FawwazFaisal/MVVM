package com.omnisoft.retrofitpractice.Utility;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by S.M.Mubbashir.A.Z. on 3/24/2021.
 */
public class ValidationUtils {

    public static boolean isEmailValid(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        Pattern pattern;
        final String Password = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*[0-9]{2}).{8,}$";
        pattern = Pattern.compile(Password);
        return pattern.matcher(password).matches();
    }
}

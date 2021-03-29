package com.omnisoft.retrofitpractice.Utility;

import android.view.View;

import com.mobsandgeeks.saripaar.ValidationError;

import java.util.List;

/**
 * Created by S.M.Mubbashir.A.Z. on 3/29/2021.
 */
public interface CustomOnClickListenerInterface {

    void postValidation(View view);

    void onValidationFailed(List<ValidationError> errors);
}

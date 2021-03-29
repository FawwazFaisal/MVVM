package com.omnisoft.retrofitpractice.Utility;

import android.view.View;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

/**
 * Created by S.M.Mubbashir.A.Z. on 3/29/2021.
 */
public class CustomOnClickListener implements Validator.ValidationListener {
    private final CustomOnClickListenerInterface listener;
    private View view;

    public CustomOnClickListener(CustomOnClickListenerInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onValidationSucceeded() {
        listener.postValidation(view);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        listener.onValidationFailed(errors);

    }
}

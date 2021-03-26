package com.omnisoft.retrofitpractice.Utility;

import android.text.Editable;
import android.view.View;

/**
 * Created by S.M.Mubbashir.A.Z. on 3/24/2021.
 */
public interface TextWatcherInterface {

    void beforeTextChanged(CharSequence s, int start, int count, int after, View view);

    void onTextChanged(CharSequence s, int start, int before, int count, View view);

    void afterTextChanged(Editable s, View view);
}

package com.omnisoft.retrofitpractice.Utility

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by S.M.Mubbashir.A.Z. on 3/24/2021.
 */
internal class TextChangeListener(private var view: View, private var textWatcher: TextWatcherInterface) : TextWatcher {

    init {
        if (view is TextInputLayout) {
            (view as TextInputLayout).editText?.addTextChangedListener(this)
        } else {
            (view as EditText).addTextChangedListener(this)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        textWatcher.beforeTextChanged(s, start, count, after, view)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textWatcher.onTextChanged(s, start, before, count, view)
    }

    override fun afterTextChanged(s: Editable?) {
        textWatcher.afterTextChanged(s, view)
    }
}
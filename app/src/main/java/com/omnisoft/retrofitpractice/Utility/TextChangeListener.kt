package com.omnisoft.retrofitpractice.Utility

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by S.M.Mubbashir.A.Z. on 3/24/2021.
 */
class TextChangeListener(private var textWatcher: TextWatcherInterface) : TextWatcher {
    lateinit var mView: View

    fun removeTextChangeListener(view: View) {
        mView = view
        if (view is EditText)
            view.removeTextChangedListener(this)
        else
            (view as TextInputLayout).editText?.removeTextChangedListener(this)
    }

    fun addTextChangeListener(view: View) {
        mView = view
        if (view is EditText)
            view.addTextChangedListener(this)
        else
            (view as TextInputLayout).editText?.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        textWatcher.beforeTextChanged(s, start, count, after, mView, mView.id)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textWatcher.onTextChanged(s, start, before, count, mView, mView.id)
    }

    override fun afterTextChanged(s: Editable?) {
        textWatcher.afterTextChanged(s, mView, mView.id)
    }
}
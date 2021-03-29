package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.omnisoft.retrofitpractice.Utility.TextChangeListener
import com.omnisoft.retrofitpractice.Utility.TextWatcherInterface
import com.omnisoft.retrofitpractice.databinding.RegistrationStep2FragmentBinding

class RegistrationStep2 : Fragment(), View.OnFocusChangeListener, TextWatcherInterface {
    lateinit var bd: RegistrationStep2FragmentBinding
    var ignoreTextWatcher: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bd = RegistrationStep2FragmentBinding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        ignoreTextWatcher = false
        bd.mobileNo.editText?.onFocusChangeListener = this
        TextChangeListener(this).addTextChangeListener(bd.mobileNo.editText!!)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            if (bd.mobileNo.editText?.text.toString().isEmpty()) {
                bd.mobileNo.editText?.setText("+92 ")
                bd.mobileNo.editText?.setSelection(bd.mobileNo.editText?.text.toString().lastIndex)
            } else {
                if (!bd.mobileNo.editText?.text.toString().startsWith("+92 ")) {
                    bd.mobileNo.editText?.text?.clear()
                    bd.mobileNo.editText?.setText("+92 ")
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int, view: View?, id: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int, view: View?, id: Int) {

    }

    override fun afterTextChanged(s: Editable?, view: View?, id: Int) {

    }
}
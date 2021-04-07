package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Or
import com.mobsandgeeks.saripaar.annotation.Pattern
import com.omnisoft.retrofitpractice.Activities.PostRegistrationForm
import com.omnisoft.retrofitpractice.R
import com.omnisoft.retrofitpractice.Utility.TextChangeListener
import com.omnisoft.retrofitpractice.Utility.TextWatcherInterface
import com.omnisoft.retrofitpractice.databinding.RegistrationStep2FragmentBinding

class RegistrationStep2 : Fragment(), TextWatcherInterface {
    lateinit var bd: RegistrationStep2FragmentBinding
    lateinit var validator: Validator

    @NotEmpty(message = "Please enter Phone No")
    @Or
    @Pattern(regex = "^3[0-4][0-9][0-9]{7}$", message = "Invalid Mobile No.")
    @Length(min = 13, max = 13)
    lateinit var mobileNo: EditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bd = RegistrationStep2FragmentBinding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mobileNo = bd.mobileNo.editText!!
        setListeners()
    }

    private fun setListeners() {
        TextChangeListener(this).addTextChangeListener(bd.mobileNo)
    }

    fun validate() {
        validator = Validator(this)
        validator.setValidationListener((requireActivity() as PostRegistrationForm))
        validator.validate()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int, view: View?, id: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int, view: View?, id: Int) {

    }

    override fun afterTextChanged(s: Editable?, view: View?, id: Int) {
        view?.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_enabled)
        (requireActivity() as PostRegistrationForm).user.phoneNo = (view as TextInputLayout).editText?.text.toString()
    }
}
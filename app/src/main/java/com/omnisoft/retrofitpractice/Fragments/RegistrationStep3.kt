package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.omnisoft.retrofitpractice.Activities.PostRegistrationForm
import com.omnisoft.retrofitpractice.databinding.RegistrationStep3FragmentBinding

class RegistrationStep3 : Fragment(), View.OnClickListener {

    lateinit var auth: FirebaseAuth
    lateinit var bd: RegistrationStep3FragmentBinding

    @NotEmpty(message = "Please enter the OTP sent via SMS")
    @Length(min = 6, max = 6, message = "Must be exactly 6 digits")
    lateinit var otp: EditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        bd = RegistrationStep3FragmentBinding.inflate(layoutInflater)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        otp = bd.otp.editText!!
        bd.signUp.setOnClickListener(this)
        bd.resendOtp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == bd.signUp.id) {
            if (otp.text.length == 6) {
                (requireActivity() as PostRegistrationForm).setPhoneAuthCred(otp.text.toString())
            }
        } else if (v?.id == bd.resendOtp.id) {
            bd.otp.editText?.text?.clear()
            (requireActivity() as PostRegistrationForm).executeMobileVerification()
        }
    }
}
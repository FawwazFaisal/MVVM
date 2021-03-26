package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omnisoft.retrofitpractice.MVVM.RegistrationStep3ViewModel
import com.omnisoft.retrofitpractice.R

class RegistrationStep3 : Fragment() {

    companion object {
        fun newInstance() = RegistrationStep3()
    }

    private lateinit var viewModel: RegistrationStep3ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.registration_step3_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationStep3ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
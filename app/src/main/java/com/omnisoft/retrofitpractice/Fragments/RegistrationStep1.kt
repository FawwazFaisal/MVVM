package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omnisoft.retrofitpractice.MVVM.RegistrationStep1ViewModel
import com.omnisoft.retrofitpractice.R

class RegistrationStep1 : Fragment() {

    companion object {
        fun newInstance() = RegistrationStep1()
    }

    private lateinit var viewModel: RegistrationStep1ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.registration_step1_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationStep1ViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
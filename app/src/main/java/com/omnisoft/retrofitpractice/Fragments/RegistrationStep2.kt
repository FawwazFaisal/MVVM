package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omnisoft.retrofitpractice.MVVM.RegistrationStep2ViewModel
import com.omnisoft.retrofitpractice.R

class RegistrationStep2 : Fragment() {

    companion object {
        fun newInstance() = RegistrationStep2()
    }

    private lateinit var viewModel: RegistrationStep2ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.registration_step2_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationStep2ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
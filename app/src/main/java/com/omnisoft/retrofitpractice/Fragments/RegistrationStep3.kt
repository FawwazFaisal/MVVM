package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.omnisoft.retrofitpractice.Activities.PostRegistrationForm
import com.omnisoft.retrofitpractice.Utility.Snack.CustomSnack
import com.omnisoft.retrofitpractice.databinding.RegistrationStep3FragmentBinding

class RegistrationStep3 : Fragment(), View.OnClickListener {

    lateinit var auth: FirebaseAuth
    lateinit var bd: RegistrationStep3FragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bd = RegistrationStep3FragmentBinding.inflate(layoutInflater)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        bd.signUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == bd.signUp.id) {
            createAccountAndAddToDB()
        }
    }

    private fun createAccountAndAddToDB() {
        createAccount()
    }

    private fun createAccount() {
        auth.createUserWithEmailAndPassword((requireActivity() as PostRegistrationForm).email, (requireActivity() as PostRegistrationForm).pass).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                addToDB()
            } else {
                CustomSnack.showSnackbar(requireActivity(), "Something went wrong", "DISMISS")
            }
        })
    }

    private fun addToDB() {
        FirebaseFirestore.getInstance().collection("users")
                .document((requireActivity() as PostRegistrationForm).email)
                .set((requireActivity() as PostRegistrationForm).user)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                    } else {

                    }
                }
    }
}
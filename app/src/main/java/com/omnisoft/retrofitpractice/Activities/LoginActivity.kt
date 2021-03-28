package com.omnisoft.retrofitpractice.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.omnisoft.retrofitpractice.R
import com.omnisoft.retrofitpractice.Utility.SharedPreferences
import com.omnisoft.retrofitpractice.Utility.TextChangeListener
import com.omnisoft.retrofitpractice.Utility.TextWatcherInterface
import com.omnisoft.retrofitpractice.Utility.ValidationUtils
import com.omnisoft.retrofitpractice.databinding.ActivityLoginBinding

const val TAG = "LOGIN"

class LoginActivity : AppCompatActivity(), TextWatcherInterface {

    lateinit var bd: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var authSateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bd.root)
        auth = Firebase.auth
        authSateListener = FirebaseAuth.AuthStateListener { it ->
            val currentUser = it.currentUser
            if (currentUser != null) {
                FirebaseFirestore.getInstance().collection("users").document(currentUser.email.toString()).get().addOnCompleteListener { it ->
                    if (it.isComplete && it.result.exists()) {
                        loginUser()
                    } else {
                        auth.signOut()
                    }
                }
            }
        }
        setListener()
    }

    private fun createNewAccount() {
        SharedPreferences.getPrefs().edit().putString("email", bd.email.editText?.text.toString()).apply()
        startActivity(Intent(this, PostRegistrationForm::class.java))
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(bd.email.editText?.text.toString(), bd.password.editText?.text.toString()).addOnCompleteListener(OnCompleteListener {
            SharedPreferences.getPrefs().edit().putString("email", bd.email.editText?.text.toString()).apply()
        })
    }

    private fun setListener() {
        bd.login.setOnClickListener {
            when (it.id) {
                bd.login.id ->
                    validateAndAuth()
            }
        }
        TextChangeListener(bd.email, this)
        TextChangeListener(bd.password, this)
    }

    private fun setUpAccount() {
        auth.createUserWithEmailAndPassword(bd.email.editText?.text.toString(), bd.password.editText?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        createNewAccount()
                    } else {
                        Toast.makeText(baseContext, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    @Override
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authSateListener)
    }

    private fun validateAndAuth() {
        if (!ValidationUtils.isEmailValid(bd.email.editText?.text.toString())) {
            bd.email.background = ContextCompat.getDrawable(this, R.drawable.edit_text_error)
        } else if (!ValidationUtils.isPasswordValid(bd.password.editText?.text.toString())) {
            bd.password.background = ContextCompat.getDrawable(this, R.drawable.edit_text_error)
        } else {
            setUpAccount()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int, view: View?) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int, view: View?) {
    }

    override fun afterTextChanged(s: Editable?, view: View?) {
        if (view?.id == bd.email.id) {
            bd.email.background = ContextCompat.getDrawable(this, R.drawable.edit_text_enabled)
        } else if (view?.id == bd.password.id) {
            bd.password.background = ContextCompat.getDrawable(this, R.drawable.edit_text_enabled)
        }
    }
}
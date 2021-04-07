package com.omnisoft.retrofitpractice.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.omnisoft.retrofitpractice.App
import com.omnisoft.retrofitpractice.R
import com.omnisoft.retrofitpractice.Room.User
import com.omnisoft.retrofitpractice.Utility.TextChangeListener
import com.omnisoft.retrofitpractice.Utility.TextWatcherInterface
import com.omnisoft.retrofitpractice.Utility.ValidationUtils
import com.omnisoft.retrofitpractice.databinding.ActivityLoginBinding

const val TAG = "LOGIN"

class LoginActivity : BaseActivity(), TextWatcherInterface {

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
                when (currentUser.email!!.toString().length) {
                    0 -> {
                        currentUser.delete()
                        return@AuthStateListener
                    }
                }
                FirebaseFirestore.getInstance().collection("users").document(currentUser.email!!).get().addOnCompleteListener { it ->
                    if (it.result.exists()) {
                        App.user = it.result.toObject(User::class.java)
                        initFCM(currentUser.email!!)
                    } else {
                        currentUser.delete()
                    }
                }
            }
        }
        setListener()
    }

    private fun createNewAccount() {
        val intent = Intent(this, PostRegistrationForm::class.java)
        intent.putExtra("email", bd.email.editText?.text.toString())
        intent.putExtra("pass", bd.password.editText?.text.toString())
        startActivity(Intent(intent))
    }

    private fun loginUser() {
        auth.removeAuthStateListener(authSateListener)
        val email = bd.email.editText?.text.toString()
        FirebaseFirestore.getInstance().collection("users").document(email).get().addOnCompleteListener(OnCompleteListener {
            if (it.result.exists()) {
                App.user = it.result.toObject(User::class.java)
                auth.signInWithEmailAndPassword(email, bd.password.editText?.text.toString()).addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        initFCM(email)
                    }
                }
            } else {
                auth.currentUser!!.delete()
                createNewAccount()
            }
        })
    }

    private fun initFCM(email: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.isNotEmpty()) {
                    App.user.fcmToken = it.result
                    FirebaseFirestore.getInstance().document(email).update("FCMToken", it.result).addOnSuccessListener {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        })
    }

    private fun setListener() {
        bd.login.setOnClickListener {
            when (it.id) {
                bd.login.id -> {
                    validateAndAuth()
                }
            }
        }
        TextChangeListener(this).addTextChangeListener(bd.email)
        TextChangeListener(this).addTextChangeListener(bd.password)
    }

    @Override
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authSateListener)
    }

    private fun validateAndAuth() {
        auth.removeAuthStateListener(authSateListener)
        if (!ValidationUtils.isEmailValid(bd.email.editText?.text.toString())) {
            bd.email.background = ContextCompat.getDrawable(this, R.drawable.edit_text_error)
        } else if (!ValidationUtils.isPasswordValid(bd.password.editText?.text.toString())) {
            bd.password.background = ContextCompat.getDrawable(this, R.drawable.edit_text_error)
        } else {
            loginUser()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int, view: View?, id: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int, view: View?, id: Int) {

    }

    override fun afterTextChanged(s: Editable?, view: View?, id: Int) {
        view?.background = ContextCompat.getDrawable(this, R.drawable.edit_text_enabled)
    }
}
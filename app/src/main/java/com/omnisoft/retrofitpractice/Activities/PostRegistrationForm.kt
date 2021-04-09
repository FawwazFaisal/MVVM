package com.omnisoft.retrofitpractice.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.omnisoft.retrofitpractice.Adapters.FragmentAdapter
import com.omnisoft.retrofitpractice.Adapters.RegistrationFormAdapter
import com.omnisoft.retrofitpractice.App
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep1
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep2
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep3
import com.omnisoft.retrofitpractice.R
import com.omnisoft.retrofitpractice.Utility.ActionCode
import com.omnisoft.retrofitpractice.Utility.Snack.CustomSnack
import com.omnisoft.retrofitpractice.databinding.ActivityPostRegistrationFormBinding
import java.util.concurrent.TimeUnit

class PostRegistrationForm : BaseActivity(), ViewPager.OnPageChangeListener, Validator.ValidationListener, View.OnClickListener {
    lateinit var bd: ActivityPostRegistrationFormBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var credential: PhoneAuthCredential
    private val phoneAuthCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            (supportFragmentManager.fragments[2] as RegistrationStep3).bd.otp.editText?.setText(credential.smsCode?.toString())
            createAccount()
        }

        override fun onVerificationFailed(e: FirebaseException) {
            bd.viewPager.setCurrentItem(1, true)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            bd.viewPager.setCurrentItem(2, true)
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        bd = ActivityPostRegistrationFormBinding.inflate(layoutInflater)
        setContentView(bd.root)
        super.onCreate(savedInstanceState)
        val email = intent.extras!!.getString("email", "")
        App.getUser().email = email
        auth = FirebaseAuth.getInstance()
        setUpViewPager()
        setListeners()
    }

    private fun setListeners() {
        bd.navLeft.setOnClickListener(this)
        bd.navRight.setOnClickListener(this)
    }

    private fun setUpViewPager() {
        val adapter = RegistrationFormAdapter(supportFragmentManager, FragmentAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.addFragment("step1", RegistrationStep1())
        adapter.addFragment("step2", RegistrationStep2())
        adapter.addFragment("step3", RegistrationStep3())
        bd.viewPager.adapter = adapter
        bd.viewPager.setPagingEnabled(false)
        bd.viewPager.offscreenPageLimit = 2
        bd.viewPager.addOnPageChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            bd.navLeft.id -> moveLeft()
            bd.navRight.id -> moveRight()
        }
    }

    fun executeMobileVerification() {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)

//        if(resendToken!=null){
//            optionsBuilder.setForceResendingToken(resendToken)
//        }
        val options: PhoneAuthOptions = optionsBuilder
                .setPhoneNumber(App.getUser().phoneNo)   // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                // Activity (for callback binding)
                .setCallbacks(phoneAuthCallback) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun setPhoneAuthCred(code: String) {
        credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        if (code == credential.smsCode) {
            createAccount()
        } else {
            //Add custom snackbar with cancel button as well
            CustomSnack.showSnackbar(this, "Invalid Code", "RESEND")
        }
    }

    fun createAccount() {
        val pass = intent.extras!!.getString("pass", "")
        auth.createUserWithEmailAndPassword(App.getUser().email, pass).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                auth.signOut()
                addToDb()
            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToDb() {
        FirebaseFirestore.getInstance().collection("user").document(App.getUser().email).set(App.getUser()).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val actionCode = ActionCode().actionCodeSettings
                        auth.sendSignInLinkToEmail(App.getUser().email, actionCode).addOnCompleteListener(OnCompleteListener {
                            auth.signOut()
                            finish()
                            startActivity(Intent(this, LoginActivity::class.java))
                        })
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onValidationSucceeded() {
        when (bd.viewPager.currentItem) {
            0 -> {
                App.getUser().name = (supportFragmentManager.fragments[0] as RegistrationStep1).name.text.toString()
                App.getUser().lastName = (supportFragmentManager.fragments[0] as RegistrationStep1).lastName.text.toString()
                bd.viewPager.setCurrentItem(1, true)
            }
            1 -> {
                App.getUser().phoneNo = "+92" + (supportFragmentManager.fragments[1] as RegistrationStep2).mobileNo.text.toString()
                executeMobileVerification()
            }
        }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        for (error: ValidationError in errors!!) {
            (error.view.parent.parent as TextInputLayout).background = ContextCompat.getDrawable(App.context, R.drawable.edit_text_error)
            (error.view as EditText).error = error.getCollatedErrorMessage(this).toString()
        }
    }

    private fun moveLeft() {
        when (bd.viewPager.currentItem) {
            1 -> bd.viewPager.setCurrentItem(0, true)
            2 -> bd.viewPager.setCurrentItem(1, true)
        }
    }

    private fun moveRight() {
        when (bd.viewPager.currentItem) {
            0 -> (((bd.viewPager.adapter as RegistrationFormAdapter).getItem(0) as RegistrationStep1).validate())
            1 -> (((bd.viewPager.adapter as RegistrationFormAdapter).getItem(1) as RegistrationStep2).validate())
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> bd.title.text = ("Step 1")
            1 -> bd.title.text = ("Step 2")
            2 -> bd.title.text = ("Step 3")
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
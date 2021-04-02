package com.omnisoft.retrofitpractice.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.omnisoft.retrofitpractice.Adapters.FragmentAdapter
import com.omnisoft.retrofitpractice.Adapters.RegistrationFormAdapter
import com.omnisoft.retrofitpractice.App
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep1
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep2
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep3
import com.omnisoft.retrofitpractice.R
import com.omnisoft.retrofitpractice.Room.User
import com.omnisoft.retrofitpractice.Utility.CustomOnClickListenerInterface
import com.omnisoft.retrofitpractice.Utility.Snack.CustomSnack
import com.omnisoft.retrofitpractice.databinding.ActivityPostRegistrationFormBinding
import java.util.concurrent.TimeUnit

class PostRegistrationForm : BaseActivity(), ViewPager.OnPageChangeListener, CustomOnClickListenerInterface, View.OnClickListener {
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
            Log.d(TAG, "onVerificationCompleted:$credential")
            createAccount()
        }

        override fun onVerificationFailed(e: FirebaseException) {
            bd.viewPager.setCurrentItem(1, true)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")
            bd.viewPager.setCurrentItem(2, true)
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }
    val user: User = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityPostRegistrationFormBinding.inflate(layoutInflater)
        setContentView(bd.root)
        val email = intent.extras!!.getString("email", "")
        val pass = intent.extras!!.getString("pass", "")
        user.email = email
        user.pass = pass
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

    override fun postValidation() {
        when (bd.viewPager.currentItem) {
            0 -> {
                user.name = ((bd.viewPager.adapter as RegistrationFormAdapter).getItem(bd.viewPager.currentItem) as RegistrationStep1).name.text.toString()
                user.lastName = ((bd.viewPager.adapter as RegistrationFormAdapter).getItem(bd.viewPager.currentItem) as RegistrationStep1).lastName.text.toString()
                bd.viewPager.setCurrentItem(1, true)
            }
            1 -> {
                user.phoneNo = ((bd.viewPager.adapter as RegistrationFormAdapter).getItem(bd.viewPager.currentItem) as RegistrationStep2).mobileNo.text.toString()
                executeMobileVerification()
            }
        }
    }


    private fun executeMobileVerification() {
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(user.phoneNo)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(phoneAuthCallback)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun setPhoneAuthCred(code: String) {
        credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        if (code == credential.smsCode) {
            createAccount()
        }
    }

    private fun createAccount() {
        auth.createUserWithEmailAndPassword(user.email, user.pass).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                addToDb()
            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToDb() {
        FirebaseFirestore.getInstance().collection("user").document(user.email).set(user).addOnCompleteListener(OnCompleteListener {
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
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        for (error: ValidationError in errors!!) {
            (error.view.parent.parent as TextInputLayout).background = ContextCompat.getDrawable(App.getContext(), R.drawable.edit_text_error)
            CustomSnack.showSnackbar(this, error.getCollatedErrorMessage(this).toString(), "DISMISS")
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
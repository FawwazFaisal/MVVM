package com.omnisoft.retrofitpractice.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.textfield.TextInputLayout
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

class PostRegistrationForm : AppCompatActivity(), ViewPager.OnPageChangeListener, CustomOnClickListenerInterface, View.OnClickListener {
    lateinit var bd: ActivityPostRegistrationFormBinding
    lateinit var email: String
    lateinit var pass: String
    var user: User = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityPostRegistrationFormBinding.inflate(layoutInflater)
        setContentView(bd.root)
        email = intent.extras!!.getString("email", "")
        pass = intent.extras!!.getString("pass", "")
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

    override fun postValidation(view: View?) {
        when (bd.viewPager.currentItem) {
            0 -> bd.viewPager.setCurrentItem(1, true)
            1 -> bd.viewPager.setCurrentItem(2, true)
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
            1 -> (((bd.viewPager.adapter as RegistrationFormAdapter).getItem(1) as RegistrationStep1).validate())
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
}
package com.omnisoft.retrofitpractice.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.omnisoft.retrofitpractice.Adapters.FragmentAdapter
import com.omnisoft.retrofitpractice.Adapters.RegistrationFormAdapter
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep1
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep2
import com.omnisoft.retrofitpractice.Fragments.RegistrationStep3
import com.omnisoft.retrofitpractice.databinding.ActivityPostRegistrationFormBinding

class PostRegistrationForm : AppCompatActivity(), View.OnClickListener, ViewPager.OnPageChangeListener {
    lateinit var bd: ActivityPostRegistrationFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityPostRegistrationFormBinding.inflate(layoutInflater)
        setContentView(bd.root)
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
        bd.viewPager.offscreenPageLimit = 2
        bd.viewPager.addOnPageChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            bd.navLeft.id -> moveLeft()
            bd.navRight.id -> moveRight()
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
            0 -> bd.viewPager.setCurrentItem(1, true)
            1 -> bd.viewPager.setCurrentItem(2, true)
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
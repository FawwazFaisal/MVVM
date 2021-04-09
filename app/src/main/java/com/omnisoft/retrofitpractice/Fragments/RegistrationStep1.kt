package com.omnisoft.retrofitpractice.Fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Order
import com.omnisoft.retrofitpractice.Activities.PostRegistrationForm
import com.omnisoft.retrofitpractice.App
import com.omnisoft.retrofitpractice.R
import com.omnisoft.retrofitpractice.Utility.TextChangeListener
import com.omnisoft.retrofitpractice.Utility.TextWatcherInterface
import com.omnisoft.retrofitpractice.databinding.RegistrationStep1FragmentBinding

class RegistrationStep1 : Fragment(), TextWatcherInterface {
    @NotEmpty(message = "Please enter your first name")
    @Order(1)

    lateinit var name: EditText

    @Order(2)
    @NotEmpty(message = "Please enter your last name")
    lateinit var lastName: EditText
    lateinit var bd: RegistrationStep1FragmentBinding
    lateinit var validator: Validator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bd = RegistrationStep1FragmentBinding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = bd.Name.editText!!
        lastName = bd.LastName.editText!!
        TextChangeListener(this).addTextChangeListener(bd.Name)
        TextChangeListener(this).addTextChangeListener(bd.LastName)
    }

    fun validate() {
        validator = Validator(this)
        validator.setValidationListener((requireActivity() as PostRegistrationForm))
        validator.validate()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int, view: View?, id: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int, view: View?, id: Int) {

    }

    override fun afterTextChanged(s: Editable?, view: View?, id: Int) {
        view?.background = ContextCompat.getDrawable(App.context, R.drawable.edit_text_enabled)
        if (id == R.id.name) {
            App.getUser().name = s.toString()
        } else if (id == R.id.LastName) {
            App.getUser()
        }
    }
}
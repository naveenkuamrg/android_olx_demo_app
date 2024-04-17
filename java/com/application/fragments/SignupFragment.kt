package com.application.fragments

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.application.R
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.databinding.FragmentSignupBinding
import com.application.helper.Validator
import com.application.viewmodels.SignupViewModel
import java.io.InputStream

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding: FragmentSignupBinding

    private val viewModel: SignupViewModel by viewModels { SignupViewModel.FACTORY }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupBinding.bind(view)
        addObserver()
        binding.reenterPassword.doAfterTextChanged { text ->
            if (text.toString() != binding.passwordEdittext.text.toString()) {
                binding.reenterPasswordLayout.error = "password dose not match"
            } else {
                binding.reenterPasswordLayout.error = null
            }
        }

        binding.registerButton.setOnClickListener {

            val email = binding.emailEdittext.text.toString().trim()
            val userName = binding.name.text.toString().trim()
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val password = binding.passwordEdittext.text.toString()
            val confirmPassword = binding.reenterPassword.text.toString()



            if (!isValid(userName, email, phoneNumber, password, confirmPassword)) {
                return@setOnClickListener
            }

            viewModel.signup(userName, email, phoneNumber, password)

        }

        binding.Signin.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        val nightModeFlags = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        val imageStream: InputStream = if(!isNightMode){
            this.resources.openRawResource(R.raw.sell_zone)
        }else{
            this.resources.openRawResource(R.raw.sell_zone_night)
        }
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.logoImageView.setImageBitmap(bitmap)

    }

    private fun isValid(
        name: String, email: String, phoneNumber: String, password: String, confirmPassword: String
    ): Boolean {
        var isValid = true

        if (!Validator.isEmailValid(email)) {
            isValid = false
            binding.emailEditTextLayout.error = "Email is not valid"
        } else {
            binding.emailEditTextLayout.error = null
        }
        if (!Validator.isPhoneNumberValid(phoneNumber)) {
            isValid = false
            binding.phoneNumberLayout.error = "Phone is not Valid"
        } else {
            binding.phoneNumberLayout.error = null
        }
        val passwordError = Validator.passwordValidator(password)
        if (passwordError != null) {
            isValid = false
            binding.passwordLayout.error = passwordError
        } else {
            binding.passwordLayout.error = null
        }
        if (password != confirmPassword || confirmPassword == "") {
            isValid = false
            binding.reenterPasswordLayout.error = "not match"
            if (password == "") {
                binding.reenterPasswordLayout.error = "can't be empty"
            }

        } else {
            binding.reenterPasswordLayout.error = null
        }

        if (!Validator.doesNotContainSpecialChars(name)) {
            if (name == "") {
                binding.nameEditTextLayout.error = "Name not Should be empty"
            } else {
                binding.nameEditTextLayout.error = "Name dosen't allowed spical chareters"
            }
            isValid = false
        } else {
            binding.nameEditTextLayout.error = null
        }
        return isValid
    }

    private fun addObserver() {
        viewModel.errorMessage.observe(
            viewLifecycleOwner
        ) { value -> binding.emailEditTextLayout.error = value }
        viewModel.userId.observe(viewLifecycleOwner) { value ->
            parentFragmentManager.popBackStack()
            val sharedPreferences = requireContext().getSharedPreferences(
                "mySharePref",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            sharedPreferences.putString("userId", value.toString())
            sharedPreferences.putString("userName",binding.name.text.toString())
            sharedPreferences.apply()
            val homeTransaction = parentFragmentManager.beginTransaction()
            homeTransaction.replace(R.id.main_view_container, MainFragment())
            homeTransaction.commit()
        }
    }

}
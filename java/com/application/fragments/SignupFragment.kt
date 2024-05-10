package com.application.fragments

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.application.R
import androidx.fragment.app.viewModels
import com.application.databinding.FragmentSignupBinding
import com.application.exceptions.InvalidUserDataException
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
            if (text.toString() != binding.passwordEdittext.text.toString() &&
                viewModel.secondAttempt
            ) {
                binding.reenterPasswordLayout.error = "Passwords do not match"
            } else {
                if (binding.reenterPassword.text.toString() != "") {
                    binding.reenterPasswordLayout.error = null
                }
            }
        }
        binding.passwordEdittext.doAfterTextChanged {
            val passwordError = Validator.passwordValidator(it.toString())
            if (viewModel.secondAttempt &&
                passwordError != null && binding.passwordEdittext.text.toString() != ""
            ) {
                binding.passwordLayout.error = passwordError
            } else {
                binding.passwordLayout.error = null
            }
        }

        binding.phoneNumber.doAfterTextChanged {
            if (!Validator.isPhoneNumberValid(it.toString())) {
                if (binding.phoneNumber.text.toString() != "" &&
                    viewModel.secondAttempt
                ) {
                    binding.phoneNumberLayout.error = "Phone number is not valid"
                }
            } else {
                binding.phoneNumberLayout.error = null
            }
        }

        binding.emailEdittext.doAfterTextChanged {
            if (!Validator.isEmailValid(it.toString())) {
                if (binding.emailEdittext.text.toString() != "" &&
                    viewModel.secondAttempt
                ) {
                    binding.emailEditTextLayout.error = "Email is not valid"
                }

            } else {
                binding.emailEditTextLayout.error = null
            }
        }

        binding.name.doAfterTextChanged {
            if (!Validator.doesNotContainSpecialChars(it.toString())) {
                //error
                if (binding.name.text.toString() != "") {
                    binding.nameEditTextLayout.error = "Name does not contain special charters"
                }
            } else {
                //non - error
                binding.nameEditTextLayout.error = null
            }
        }

        binding.registerButton.setOnClickListener {

            val email = binding.emailEdittext.text.toString().trim()
            val userName = binding.name.text.toString().trim()
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val password = binding.passwordEdittext.text.toString()
            val confirmPassword = binding.reenterPassword.text.toString()




            if (!isValid(userName, email, phoneNumber, password, confirmPassword)) {
                viewModel.secondAttempt = true
                return@setOnClickListener
            }

            viewModel.signup(userName, email, phoneNumber, password)
            viewModel.secondAttempt = false

        }

        binding.Signin.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val nightModeFlags =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        val imageStream: InputStream = if (!isNightMode) {
            this.resources.openRawResource(R.raw.sell_zone)
        } else {
            this.resources.openRawResource(R.raw.sell_zone_night)
        }
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.logoImageView.setImageBitmap(bitmap)

    }

    private fun isValid(
        name: String, email: String, phoneNumber: String, password: String, confirmPassword: String
    ): Boolean {
        var isValid = true


        if (password != confirmPassword || confirmPassword == "") {
            isValid = false
            binding.reenterPassword.requestFocus()
            binding.reenterPasswordLayout.error = "Passwords do not match"
            if (confirmPassword == "") {
                binding.reenterPasswordLayout.error = "Password can't be empty"
            }

        } else {
            binding.reenterPasswordLayout.error = null
        }

        val passwordError = Validator.passwordValidator(password)
        if (passwordError != null) {
            binding.passwordEdittext.requestFocus()
            isValid = false
            binding.passwordLayout.error = passwordError
        } else {
            binding.passwordLayout.error = null
        }

        if (!Validator.isPhoneNumberValid(phoneNumber)) {
            isValid = false
            binding.phoneNumberLayout.error = "Phone Number is not Valid"
            if (phoneNumber == "") {
                binding.phoneNumberLayout.error = "Phone Number can't be empty"
            }
            binding.phoneNumber.requestFocus()
        } else {
            binding.phoneNumberLayout.error = null
        }

        if (!Validator.doesNotContainSpecialChars(name)) {
            if (name == "") {
                binding.nameEditTextLayout.error = "Name can't be empty"
            } else {
                binding.nameEditTextLayout.error = "Name shouldn't contain special characters"
            }
            binding.name.requestFocus()
            isValid = false
        } else {
            binding.nameEditTextLayout.error = null
        }


        if (!Validator.isEmailValid(email)) {
            isValid = false
            binding.emailEditTextLayout.error = "Email is not valid"
            if (email == "") {
                binding.emailEditTextLayout.error = "Email can't be empty"
            }
            binding.emailEdittext.requestFocus()
        } else {
            binding.emailEditTextLayout.error = null
        }

        return isValid
    }

    private fun addObserver() {
        viewModel.exception.observe(
            viewLifecycleOwner
        ) { value ->
            val emailEditTextLayout = binding.emailEditTextLayout
            val phoneNumberTextLayout = binding.phoneNumberLayout
            emailEditTextLayout.error = null
            phoneNumberTextLayout.error = null
            when (value) {
                is InvalidUserDataException.EmailAlreadyExists -> {
                    emailEditTextLayout.error = value.message
                }

                is InvalidUserDataException.PhoneNumberAlreadyRegistered -> {
                    phoneNumberTextLayout.error = value.message
                }
            }
        }
        viewModel.userId.observe(viewLifecycleOwner) { value ->
            parentFragmentManager.popBackStack()
            val sharedPreferences = requireContext().getSharedPreferences(
                "mySharePref",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            sharedPreferences.putString("userId", value.toString())
            sharedPreferences.putString("userName", binding.name.text.toString())
            sharedPreferences.apply()
            val homeTransaction = parentFragmentManager.beginTransaction()
            homeTransaction.replace(R.id.main_view_container, MainFragment())
            homeTransaction.commit()
        }
    }

}
package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.application.R
import com.application.databinding.FragmentLoginBinding
import com.application.exceptions.AuthenticationSignInException
import com.application.helper.Validator
import com.application.viewmodels.SignInViewModel


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: SignInViewModel by viewModels { SignInViewModel.FACTORY }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        addObserve()
        binding.signup.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_view_container, SignupFragment())
            fragmentTransaction.addToBackStack("addSignupPage")
            fragmentTransaction.commit()
        }

        binding.signin.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString()
            if (!Validator.isEmailValid(email) || email == "") {
                if (email == "") {
                    binding.emailEdittextLayout.error = "Email not should be empty"
                } else {
                    binding.emailEdittextLayout.error = "Email is not valid"
                }
                return@setOnClickListener
            } else {
                binding.emailEdittextLayout.error = null
            }

            viewModel.signIn(email, password)
        }
    }


    private fun addObserve() {
        viewModel.user.observe(
            viewLifecycleOwner
        ) { value ->
            Log.i("TAG", value.name)
            val sharedPreferences = requireContext().getSharedPreferences(
                "mySharePref",
                AppCompatActivity.MODE_PRIVATE
            )
            sharedPreferences.edit().apply {
                putString("userId", value.id.toString())
                putString("userName", value.name)
                apply()
            }
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_view_container, MainFragment())
            fragmentTransaction.commit()
        }

        viewModel.exceptions.observe(
            viewLifecycleOwner) { value ->
            when (value) {
                is AuthenticationSignInException.UserNotFoundAuthenticationException -> {
                    binding.emailEdittextLayout.error = value.message
                }

                is AuthenticationSignInException.PasswordInvalidAuthenticationException -> {
                    binding.passwordEdittextLayout.error = value.message
                }

            }
        }
    }


}

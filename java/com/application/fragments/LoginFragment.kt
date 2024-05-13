package com.application.fragments

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.databinding.FragmentLoginBinding
import com.application.exceptions.AuthenticationSignInException
import com.application.helper.Utility
import com.application.viewmodels.SignInViewModel
import java.io.InputStream


class LoginFragment : Fragment(R.layout.fragment_login) {


    private lateinit var binding: FragmentLoginBinding
    private val viewModel: SignInViewModel by viewModels { SignInViewModel.FACTORY }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        addObserve()

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES

        val imageStream: InputStream = if (!isNightMode) {
            this.resources.openRawResource(R.raw.sell_zone)
        } else {
            this.resources.openRawResource(R.raw.sell_zone_night)
        }
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.logoImageView.setImageBitmap(bitmap)
        setOnClickListener()

//        Utility.removeErrorAfterTextChanged(binding.emailEdittext, binding.emailEdittextLayout)
//        Utility.removeErrorAfterTextChanged(
//            binding.passwordEdittext,
//            binding.passwordEdittextLayout
//        )


    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            binding.emailEdittextLayout.error = null
            binding.passwordEdittextLayout.error = null
        }
    }

    private fun setOnClickListener() {
        binding.Signin.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString()
            if (email == "") {
                binding.emailEdittextLayout.error = "Field not should be empty"
                return@setOnClickListener
            } else {
                binding.emailEdittextLayout.error = null
            }
            viewModel.signIn(email, password)
        }
        binding.Signup.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_view_container, SignupFragment())
            fragmentTransaction.addToBackStack("addSignupPage")
            fragmentTransaction.commit()
        }
    }

    private fun addObserve() {
        viewModel.user.observe(
            viewLifecycleOwner
        ) { value ->
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
            viewLifecycleOwner
        ) { value ->
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

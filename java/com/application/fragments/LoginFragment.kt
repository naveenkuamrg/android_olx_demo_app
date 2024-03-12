package com.application.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.application.R
import com.application.databinding.FragmentLoginBinding
import com.application.exceptions.AuthenticationSignInExceptions
import com.application.helper.Validator
import com.application.viewmodels.SignInViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: SignInViewModel by activityViewModels { SignInViewModel.FACTORY }
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
                if(email == ""){
                    binding.emailEdittextLayout.error = "Email not should be empty"
                }else{
                    binding.emailEdittextLayout.error = "Email is not valid"
                }
                return@setOnClickListener
            } else {
                binding.emailEdittextLayout.error = null
            }

            viewModel.signIn(email,password)
        }
    }


    fun addObserve(){
        viewModel.userId.observe(viewLifecycleOwner,object : Observer<Long>{
            override fun onChanged(value: Long) {
                if(value.toInt() != -1) {
                    val sharedPreferences=requireContext().getSharedPreferences("mySharePref",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    sharedPreferences.edit().apply {
                        putString("userId", value.toString())
                        apply()
                    }
                    val fragmentTransaction = parentFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.main_view_container, MainFragment())
                    fragmentTransaction.commit()
                }
            }
        })

        viewModel.exceptions.observe(
            viewLifecycleOwner,object : Observer<AuthenticationSignInExceptions>{
            override fun onChanged(value: AuthenticationSignInExceptions) {
               when(value){
                   is AuthenticationSignInExceptions.UserNotFoundAuthenticationException -> {
                       binding.emailEdittextLayout.error = value.message
                   }
                   is AuthenticationSignInExceptions.PasswordInvalidAuthenticationException ->{
                       binding.passwordEdittextLayout.error = value.message
                   }
               }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyUserId()
    }
}

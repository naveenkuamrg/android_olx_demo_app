package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.application.R
import androidx.fragment.app.activityViewModels
import com.application.databinding.FragmentSignupBinding
import com.application.viewmodels.SignupViewModel

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding : FragmentSignupBinding

    private  val viewModel : SignupViewModel by activityViewModels {SignupViewModel.FACTORY}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupBinding.bind(view)


        binding.reenterPassword.setOnFocusChangeListener { _, _ ->
//            binding.reenterPassword.setError("password dosn't match")
            Log.i("TAG","${binding.password.text.toString()} - ${binding.reenterPassword.text.toString()}")
            if(binding.password.text.toString() != binding.reenterPassword.text.toString()){
                binding.reenterPasswordError.apply {
                    binding.reenterPasswordLayout.error ="dfjbdsfjbes"
                    visibility = View.VISIBLE
                    text = "password dosn't match"
                }
            }else{
                binding.reenterPasswordError.text = ""
                binding.reenterPasswordLayout.error = null
                binding.reenterPasswordError.visibility = View.GONE
            }
        }




        binding.register.setOnClickListener {


            var isValid = true
            val email = binding.email.text.toString().trim()
            val userName = binding.name.text.toString().trim()
            val phoneNumber = binding.name.text.toString().trim()
            val password = binding.name.text.toString()
            val confirmPassword = binding.reenterPassword.text.toString()
            if(password != confirmPassword){
                return@setOnClickListener
            }

            val result = viewModel.signup(email,userName,phoneNumber,password)

            Log.i("TAG",result.toString())
        }


    }
}
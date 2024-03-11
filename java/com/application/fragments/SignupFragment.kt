package com.application.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.application.R
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.application.databinding.FragmentSignupBinding
import com.application.helper.Validator
import com.application.model.RegisterResult
import com.application.viewmodels.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding : FragmentSignupBinding

    private  val viewModel : SignupViewModel by activityViewModels {SignupViewModel.FACTORY}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupBinding.bind(view)


        binding.registerButton.setOnClickListener {

            val email = binding.email.text.toString().trim()
            val userName = binding.name.text.toString().trim()
            val phoneNumber = binding.name.text.toString().trim()
            val password = binding.name.text.toString()
            val confirmPassword = binding.reenterPassword.text.toString()

            binding.reenterPassword.addTextChangedListener{text ->
                if(text.toString() != password){

                }else{

                }
            }

            if(!isValid(userName,email, phoneNumber, password, confirmPassword)){
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.Default) {
                when(viewModel.signup(email,userName,phoneNumber,password)){
                    RegisterResult.REGISTERED_SUCCESS ->{
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_view_container,HomeFragment())
                        transaction.commit()
                    }
                    RegisterResult.ALREADY_REGISTERED ->{
                    }
                }
            }

        }


    }

    fun isValid(name: String,email: String,phoneNumber: String,password: String,confirmPassword: String):Boolean{
        var isValid = true

        if(!Validator.isEmailValid(email)){
            isValid = false
            binding.emailEditTextLayout.error = "Email is not valid"
        }else{
            binding.emailEditTextLayout.error = null
        }
        if(!Validator.isPhoneNumberValid(phoneNumber)) {
            isValid = false
            binding.phoneNumberLayout.error = "Phone is not Valid"
        }else{
            binding.phoneNumberLayout.error = null
        }
        val passwordError = Validator.passwordValidator(password)
        if( passwordError != null){
            isValid = false
            binding.passwordLayout.error = passwordError
        }else{
            binding.passwordLayout.error = null
        }
        if(password != confirmPassword){
            isValid = false
        }

        if(!Validator.doesNotContainSpecialChars(name)){
            if(name == ""){
                binding.nameEditTextLayout.error = "Name not Should be empty"
            }else{
                binding.nameEditTextLayout.error = "Name dosen't allowed spical chareters"
            }
            isValid = false
        }else{
            binding.nameEditTextLayout.error = null
        }

        return isValid
    }
}
package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.R
import com.application.databinding.FragmentEditProfileBinding
import com.application.exceptions.InvalidUserDataException
import com.application.helper.StringConverter
import com.application.helper.Validator
import com.application.viewmodels.EditProfileViewModel
import com.application.viewmodels.ProfilePageViewModel

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    lateinit var binding: FragmentEditProfileBinding

    val profilePageViewModel : ProfilePageViewModel by
            activityViewModels { ProfilePageViewModel.FACTORY }

    val editProfileViewModel : EditProfileViewModel by viewModels { EditProfileViewModel.FACTORY }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)
        setUpNavigationForToolbar()
        setObserve()
        setOnClickListenerToUpdateButton()

    }

    private fun setObserve(){
        profilePageViewModel.profile.observe(viewLifecycleOwner){value ->
            binding.emailEdittext.text = StringConverter.toEditable(value.email)
            binding.nameEdittext.text = StringConverter.toEditable(value.name)
            binding.phoneNumberEdittext.text = StringConverter.toEditable(value.phoneNumber)

        }

        editProfileViewModel.isUploaded.observe(viewLifecycleOwner){value ->
            Log.i("tag",value.toString())
            if(value){
                parentFragmentManager.popBackStack()
            }
        }

        editProfileViewModel.exception.observe(viewLifecycleOwner){ value ->
            val emailEditTextLayout = binding.emailEditTextLayout
            val phoneNumberEdittextLayout = binding.phoneNumberEdittextLayout
            emailEditTextLayout.error = null
            phoneNumberEdittextLayout.error = null
            when(value){
                is InvalidUserDataException.EmailAlreadyExists -> {
                    emailEditTextLayout.error = value.message
                }
                is InvalidUserDataException.PhoneNumberAlreadyRegistered -> {
                    phoneNumberEdittextLayout.error = value.message
                }
            }
        }
    }
    private fun setUpNavigationForToolbar(){
        val toolbar = binding.profileEditToolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
            Log.i("Tag","call back")
        }

    }

    private  fun setOnClickListenerToUpdateButton(){
        binding.updateButton.setOnClickListener {
            val name = binding.nameEdittext.text.toString()
            val email = binding.emailEdittext.text.toString()
            val phoneNumber = binding.phoneNumberEdittext.text.toString()
            var isValid = true
            if(!Validator.isEmailValid(email)){
                binding.emailEditTextLayout.error = "Email is not valid"
                isValid = false
            }else{
                binding.emailEditTextLayout.error = null
            }

            if(!Validator.isPhoneNumberValid(phoneNumber)){
                binding.phoneNumberEdittext.error = "Phone number is not valid"
                isValid = false
            }else{
                binding.phoneNumberEdittext.error = null
            }

            if(!Validator.doesNotContainSpecialChars(name)){
                binding.nameEditTextLayout.error = "name dosnt have special chater"
                isValid = false
            }else{
                binding.nameEditTextLayout.error = null
            }

            if(!isValid){
                return@setOnClickListener
            }

            editProfileViewModel.uploadProfile(name,email,phoneNumber,profilePageViewModel.profile.value!!)
        }
    }
}
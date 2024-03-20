package com.application.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.R
import com.application.databinding.FragmentEditProfileBinding
import com.application.exceptions.InvalidUserDataException
import com.application.helper.ImageConverter
import com.application.helper.StringConverter
import com.application.helper.Validator
import com.application.viewmodels.EditProfileViewModel
import com.application.viewmodels.ProfilePageViewModel
import java.io.File

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {


    //ActivityResultLauncher
    lateinit var binding: FragmentEditProfileBinding

    val profilePageViewModel: ProfilePageViewModel by
    activityViewModels { ProfilePageViewModel.FACTORY }

    val editProfileViewModel: EditProfileViewModel by viewModels { EditProfileViewModel.FACTORY }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)
        setUpNavigationForToolbar()
        setObserve()
        setOnClickListenerToUpdateButton()
        setOnClickListenerToAddImageBtn()
        setOnClickListenerToRemoveBtn()
    }

    private fun setObserve() {
        profilePageViewModel.profile.observe(viewLifecycleOwner) { value ->
            binding.emailEdittext.text = StringConverter.toEditable(value.email)
            binding.nameEdittext.text = StringConverter.toEditable(value.name)
            binding.phoneNumberEdittext.text = StringConverter.toEditable(value.phoneNumber)
            if (value.profileImage != null) {
                binding.userDp.setImageBitmap(value.profileImage)
                binding.addImageButton.apply {
                    text = "change image"
                    val drawable: Drawable = resources.getDrawable(R.drawable.ic_edit, null)
                    icon = drawable
                }
                binding.removeImageBtn.visibility = View.VISIBLE
            }
        }

        editProfileViewModel.isUploaded.observe(viewLifecycleOwner) { value ->
            if (value) {
                parentFragmentManager.popBackStack()
            }
        }

        editProfileViewModel.exception.observe(viewLifecycleOwner) { value ->
            val emailEditTextLayout = binding.emailEditTextLayout
            val phoneNumberEdittextLayout = binding.phoneNumberEdittextLayout
            emailEditTextLayout.error = null
            phoneNumberEdittextLayout.error = null
            when (value) {
                is InvalidUserDataException.EmailAlreadyExists -> {
                    emailEditTextLayout.error = value.message
                }

                is InvalidUserDataException.PhoneNumberAlreadyRegistered -> {
                    phoneNumberEdittextLayout.error = value.message
                }
            }
        }
    }

    private fun setUpNavigationForToolbar() {
        val toolbar = binding.profileEditToolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setOnClickListenerToUpdateButton() {
        binding.updateButton.setOnClickListener {
            val name = binding.nameEdittext.text.toString().trim()
            val email = binding.emailEdittext.text.toString().trim()
            val phoneNumber = binding.phoneNumberEdittext.text.toString().trim()
            var isValid = true
            if (!Validator.isEmailValid(email)) {
                binding.emailEditTextLayout.error = "Email is not valid"
                isValid = false
            } else {
                binding.emailEditTextLayout.error = null
            }

            if (!Validator.isPhoneNumberValid(phoneNumber)) {
                binding.phoneNumberEdittext.error = "Phone number is not valid"
                isValid = false
            } else {
                binding.phoneNumberEdittext.error = null
            }

            if (!Validator.doesNotContainSpecialChars(name)) {
                binding.nameEditTextLayout.error = "name dosn't have special chater"
                isValid = false
            } else {
                binding.nameEditTextLayout.error = null
            }

            if (!isValid) {
                return@setOnClickListener
            }

            editProfileViewModel.uploadProfile(
                name, email, phoneNumber, profilePageViewModel.profile.value!!
            )
        }
    }

    private fun setOnClickListenerToAddImageBtn() {
        val startActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    binding.userDp.setImageURI(it.data!!.data)

                    ImageConverter.loadBitmapFromUri(
                        requireContext(),
                        it.data!!.data!!,
                        1000,
                        1000
                    ) { bitmap ->
                        if (bitmap != null) {
                            // Do something with the bitmap
                            binding.userDp.setImageBitmap(bitmap)
                            editProfileViewModel.uploadProfileImage(
                                bitmap,
                                profilePageViewModel.profile.value!!.id
                            )
                        } else {
                            // Handle error
                            Log.e("TAG", "Failed to load bitmap from URI")
                        }
                    }

                    binding.removeImageBtn.visibility = View.VISIBLE
                    binding.addImageButton.apply {
                        text = "Change Image"
                        val drawable: Drawable = resources.getDrawable(R.drawable.ic_edit, null)
                        icon = drawable
                    }
                }
            }
        binding.addImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult.launch(intent)
        }

    }

    private fun setOnClickListenerToRemoveBtn() {
        binding.removeImageBtn.setOnClickListener {
            editProfileViewModel.deleteProfileImage(profilePageViewModel.profile.value!!.id)
            binding.userDp.setImageResource(R.drawable.ic_profile)
            binding.removeImageBtn.visibility = View.GONE
            binding.addImageButton.apply {
                text = "Add Image"
                val drawable: Drawable = resources.getDrawable(R.drawable.ic_add, null)
                icon = drawable
            }
        }
    }
}
package com.application.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.BottomSheetDialogPhotoPicker
import com.application.databinding.FragmentEditProfileBinding
import com.application.exceptions.InvalidUserDataException
import com.application.helper.StringConverter
import com.application.helper.Validator
import com.application.viewmodels.EditProfileViewModel
import com.application.viewmodels.ProfilePageViewModel

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile),
    BottomSheetDialogPhotoPicker {


    //ActivityResultLauncher
    lateinit var binding: FragmentEditProfileBinding

    private val profilePageViewModel: ProfilePageViewModel by
    activityViewModels { ProfilePageViewModel.FACTORY }

    private val editProfileViewModel: EditProfileViewModel by viewModels { EditProfileViewModel.FACTORY }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)
        setUpNavigationForToolbar()
        if (savedInstanceState == null) {
            setObserveUpdateUI()
        }
        setObserve()
        setOnClickListenerToUpdateButton()
        setOnClickListenerToAddImageBtn()
        setOnClickListenerToRemoveBtn()
    }

    private fun setObserveUpdateUI() {
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
    }

    private fun setObserve() {
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

        editProfileViewModel.tempImage.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.userDp.setImageBitmap(it)
            } else {
                binding.userDp.setImageResource(R.drawable.ic_profile_outline)
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

        binding.addImageButton.setOnClickListener {
            val f = BottomSheetDialogPhotoPicker()
            f.show(childFragmentManager, "bottomSheet")
        }

    }

    private fun setOnClickListenerToRemoveBtn() {
        binding.removeImageBtn.setOnClickListener {
            editProfileViewModel.isRemoveDp = true
            editProfileViewModel.tempImage.value = null
            binding.removeImageBtn.visibility = View.GONE
            binding.addImageButton.apply {
                text = "Add Image"
                val drawable: Drawable = resources.getDrawable(R.drawable.ic_add, null)
                icon = drawable
            }
        }
    }

    override fun getCountOfBitmapList(): Int {
        return 1
    }

    override fun setBitmap(bitmap: Bitmap) {
        binding.userDp.setImageBitmap(bitmap)
        editProfileViewModel.tempImage.value = bitmap
        binding.removeImageBtn.visibility = View.VISIBLE
        binding.addImageButton.apply {
            text = "Change Image"
            val drawable: Drawable = resources.getDrawable(R.drawable.ic_edit, null)
            icon = drawable
        }
    }
}
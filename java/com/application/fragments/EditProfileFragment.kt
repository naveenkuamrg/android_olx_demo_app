package com.application.fragments

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.PhotoPickerBottomSheet
import com.application.databinding.FragmentEditProfileBinding
import com.application.exceptions.InvalidUserDataException
import com.application.helper.StringConverter
import com.application.helper.Utility
import com.application.helper.Validator
import com.application.viewmodels.EditProfileViewModel
import com.application.viewmodels.ProfilePageViewModel
import java.io.ByteArrayOutputStream
import java.io.File


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile),
    PhotoPickerBottomSheet {
    lateinit var binding: FragmentEditProfileBinding

    private val profilePageViewModel: ProfilePageViewModel by
    activityViewModels { ProfilePageViewModel.FACTORY }

    private val editProfileViewModel: EditProfileViewModel by viewModels { EditProfileViewModel.FACTORY }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)

        if (savedInstanceState == null) {
            setObserveUpdateUI()
        }
        setUpNavigationForToolbar()
        setObserve()
        setOnClickListenerToAddImageBtn()
        setOnClickListenerToRemoveBtn()
        setUpOnBackPress()

        Utility.removeErrorAfterTextChanged(binding.nameEdittext, binding.nameEditTextLayout)
        Utility.removeErrorAfterTextChanged(binding.emailEdittext, binding.emailEditTextLayout)
        Utility.removeErrorAfterTextChanged(
            binding.phoneNumberEdittext,
            binding.phoneNumberEdittextLayout
        )

    }

    private fun onBackPress() {
        if (isDataUpdate()) {
            AlertDialog.Builder(context).apply {
                setMessage("If you go back, any changes you made will be lost")
                setPositiveButton("Confirm") { _, _ ->
                    editProfileViewModel.isDataUpdate = false
                    parentFragmentManager.popBackStack()
                }
                setNegativeButton("NO", null)
                show()
            }
        } else {
            parentFragmentManager.popBackStack()
        }
    }

    private fun isDataUpdate(): Boolean {
        val profile = profilePageViewModel.profile.value
        return isChanged(profile?.name, binding.nameEdittext.text.toString()) ||
                isChanged(profile?.email, binding.emailEdittext.text.toString()) ||
                isChanged(profile?.phoneNumber, binding.phoneNumberEdittext.text.toString()) ||
                isChanged(profile?.profileImage, editProfileViewModel.tempImage.value)
    }

    private fun <T> isChanged(productVal: T, enteredVal: T): Boolean {
        Log.i("EditProductFragment", "${productVal} val ${enteredVal}")
        if (productVal == null && enteredVal.toString().isEmpty() || enteredVal == 0) {
            return false
        }
        return productVal != enteredVal


    }

    private fun setUpOnBackPress() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPress()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun setTextChangedListener() {
        updateIsDataUpdate(binding.emailEdittext, profilePageViewModel.profile.value!!.email)
        updateIsDataUpdate(binding.nameEdittext, profilePageViewModel.profile.value!!.name)
        updateIsDataUpdate(
            binding.phoneNumberEdittext,
            profilePageViewModel.profile.value!!.phoneNumber
        )
    }

    private fun updateIsDataUpdate(editText: EditText, previousData: String) {
        editText.addTextChangedListener {
            if (it.toString() == previousData) {
                editProfileViewModel.isDataUpdate = false
                return@addTextChangedListener
            }
            editProfileViewModel.isDataUpdate = true
        }
    }

    private fun setObserveUpdateUI() {
        profilePageViewModel.profile.observe(viewLifecycleOwner) { value ->
            binding.emailEdittext.text = StringConverter.toEditable(value.email)
            binding.nameEdittext.text = StringConverter.toEditable(value.name)
            binding.phoneNumberEdittext.text = StringConverter.toEditable(value.phoneNumber)
            if (value.profileImage != null) {
                editProfileViewModel.tempImage.value = value.profileImage
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        profilePageViewModel.showProfile(Utility.getLoginUserId(requireContext()))
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
                binding.userDp.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.userDp.setImageBitmap(it)
                binding.addImageButton.apply {
                    text = "Change image"
                    val drawable: Drawable = resources.getDrawable(R.drawable.ic_edit, null)
                    icon = drawable
                }
                binding.removeImageBtn.visibility = View.VISIBLE
            } else {
                binding.userDp.scaleType = ImageView.ScaleType.CENTER_INSIDE
                binding.userDp.setImageResource(R.drawable.ic_add_image)
                binding.addImageButton.apply {
                    text = "Add image"
                    icon = null
                }
                binding.removeImageBtn.visibility = View.GONE

            }
        }
    }

    private fun setUpNavigationForToolbar() {
        val toolbar = binding.profileEditToolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            onBackPress()
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {

                    val name = binding.nameEdittext.text.toString().trim()
                    val email = binding.emailEdittext.text.toString().trim()
                    val phoneNumber = binding.phoneNumberEdittext.text.toString().trim()
                    var isValid = true
                    if (!Validator.isEmailValid(email)) {
                        binding.emailEditTextLayout.error = "Email is not valid"
                        isValid = false
                        if(binding.emailEdittext.text.toString() == ""){
                            binding.emailEditTextLayout.error = "Email can't be empty"
                        }
                    } else {
                        binding.emailEditTextLayout.error = null
                    }

                    if (!Validator.isPhoneNumberValid(phoneNumber)) {
                        binding.phoneNumberEdittextLayout.error = "Phone number is not valid"
                        if(binding.phoneNumberEdittext.text.toString() == ""){
                            binding.phoneNumberEdittextLayout.error = "Phone can't be empty"
                        }
                        isValid = false
                    } else {
                        binding.phoneNumberEdittext.error = null
                    }

                    if (!Validator.doesNotContainSpecialChars(name)) {
                        binding.nameEditTextLayout.error = "name doesn't have special character"
                        if(binding.nameEdittext.text.toString() == ""){
                            binding.nameEditTextLayout.error = "Name can't be empty"
                        }
                        isValid = false
                    } else {
                        binding.nameEditTextLayout.error = null
                    }


                    if (!isValid) {
                        return@setOnMenuItemClickListener true
                    }

                    editProfileViewModel.uploadProfile(
                        name, email, phoneNumber, profilePageViewModel.profile.value!!
                    )


                    return@setOnMenuItemClickListener true
                }
            }

            return@setOnMenuItemClickListener false
        }
    }


    private fun setOnClickListenerToAddImageBtn() {
        binding.userDp.setOnClickListener {
            val f = BottomSheetDialogPhotoPicker()
            f.show(childFragmentManager, "bottomSheet")
        }
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
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val file = File(context?.cacheDir, "profile_temp.png")
        file.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        return file
    }

    // Restoring Bitmap from file
    private fun restoreBitmapFromFile(filePath: String): Bitmap? {
        val file = File(filePath)
        if (file.exists()) {
            return BitmapFactory.decodeFile(filePath)
        }
        return null
    }

    override fun getBitmapCount(): Int {
        return 1
    }

    override fun addBitmap(bitmap: Bitmap) {
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
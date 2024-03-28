package com.application.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.application.R
import com.application.callbacks.BottomSheetDialogPhotoPicker
import com.application.databinding.FragmentBottomSheetDialogPhotoPickerBinding
import com.application.helper.ImageConverter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogPhotoPicker :
    BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_dialog_photo_picker) {
    lateinit var callback: BottomSheetDialogPhotoPicker
    lateinit var binding: FragmentBottomSheetDialogPhotoPickerBinding
    private lateinit var startActivityForResultProductImages:
            ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var startActivityForResultProductImage:
            ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var imageRegister: ActivityResultLauncher<Intent>
    private lateinit var photoUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = parentFragment as BottomSheetDialogPhotoPicker
        setRegisterForActivityResult(callback.getCountOfBitmapList())
    }

    private fun setRegisterForActivityResult(count: Int) {
        if (count == 1) {
            startActivityForResultProductImage =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        ImageConverter.loadBitmapFromUri(
                            requireContext(),
                            uri, 1000, 1000
                        ) { bitmap ->
                            if (bitmap != null) {
                                callback.setBitmap(bitmap)
                            }
                        }
                    }
                    this.dismiss()
                }
        } else {
            startActivityForResultProductImages = registerForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia(
                     count
                )
            ) {
                for (uri in it) {
                    ImageConverter.loadBitmapFromUri(
                        requireContext(),
                        uri,
                        1000,
                        1000
                    ) { it1 ->
                        if (it1 != null) {
                            callback.setBitmap(it1)
                        }
                    }
                }
                this.dismiss()
            }
        }

        imageRegister =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    callback.setBitmap(it.data?.extras?.get("data") as Bitmap)
                }

                this.dismiss()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetDialogPhotoPickerBinding.bind(view)
        setOnClickListener()
    }


    private fun setOnClickListener() {
        binding.galleryBtn.setOnClickListener {
            if (callback.getCountOfBitmapList() > 1) {
                startActivityForResultImages()
            } else {
                startActivityForResultProductImage()
            }
        }
        binding.cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            imageRegister.launch(intent)
        }
    }

    private fun startActivityForResultImages() {

        startActivityForResultProductImages.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )

    }

    private fun startActivityForResultProductImage() {
        startActivityForResultProductImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}
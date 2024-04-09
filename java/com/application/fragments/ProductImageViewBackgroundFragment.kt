package com.application.fragments

import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.application.R
import com.application.callbacks.PhotoPickerBottomSheet
import com.application.helper.ImageConverter

class ProductImageViewBackgroundFragment() : Fragment(R.layout.fragment_home) {


    private lateinit var callBack: PhotoPickerBottomSheet


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callBack = parentFragment as PhotoPickerBottomSheet

        val numberToUpload = callBack.getBitmapCount()
        if (numberToUpload < 4) {
            startActivityForResultImages(numberToUpload, savedInstanceState)
        } else {
            startActivityForResultProductImage(savedInstanceState)
        }

    }

    private fun startActivityForResultProductImage(savedInstanceState: Bundle?) {
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                ImageConverter.loadBitmapFromUri(
                    requireContext(),
                    uri, 1000, 1000
                ) { bitmap ->
                    if (bitmap != null) {
                        callBack.addBitmap(bitmap)
                    }
                }
            }
//                this.dismissNow()
            parentFragmentManager.popBackStack()
        }.apply {
            if (savedInstanceState == null) {
                launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }


    }

    private fun startActivityForResultImages(count: Int, savedInstanceState: Bundle?) {
        val startActivityForResultProductImages = registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                5 - count
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
                        callBack.addBitmap(it1)
                    }
                }
            }
            parentFragmentManager.popBackStack()
        }
        if (savedInstanceState == null) {
            startActivityForResultProductImages.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }
}

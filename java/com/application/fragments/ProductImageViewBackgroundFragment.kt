package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.callbacks.ProductImageViewBackgroundFragmentCallBack
import com.application.helper.ImageConverter
import com.application.viewmodels.EditProductViewModel

class ProductImageViewBackgroundFragment : Fragment() {

    lateinit var callBack: ProductImageViewBackgroundFragmentCallBack
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val numberToUpload = callBack.getCountOfBitmapList()
        if (numberToUpload < 4) {
            startActivityForResultProductImages(numberToUpload)
        } else {
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if(uri != null) {
                    ImageConverter.loadBitmapFromUri(
                        requireContext(),
                        uri, 1000, 1000
                    ) { bitmap ->
                        if (bitmap != null) {
                            callBack.setBitmap(bitmap)
                        }
                    }
                }
                parentFragmentManager.popBackStack()
            }.apply {
                launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

    }

    fun startActivityForResultProductImages(count: Int) {
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
                        callBack.setBitmap(it1)
                    }
                }
            }
            parentFragmentManager.popBackStack()
        }

        startActivityForResultProductImages.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }


}
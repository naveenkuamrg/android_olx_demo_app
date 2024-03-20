package com.application.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.adapter.ImageViewAdapter
import com.application.databinding.FragmentEditProductBinding
import com.application.helper.ImageConverter
import com.application.model.ProductType
import com.application.viewmodels.EditProductViewModel

class EditProductFragment : Fragment(R.layout.fragment_edit_product) {

    lateinit var bindind: FragmentEditProductBinding
    private val editProfileViewModel: EditProductViewModel by viewModels {
        EditProductViewModel.FACTORY
    }
    val startActivityForResultProductImages: ActivityResultLauncher<PickVisualMediaRequest>  =
        registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                5
            )
        ) {
            val bitmaps: MutableList<Bitmap> = mutableListOf()
            for (uri in it) {
                ImageConverter.loadBitmapFromUri(
                    requireContext(),
                    uri,
                    1000,
                    1000
                ) { it1 ->
                    Log.i("tag imag",it1.toString())
                    if (it1 != null) {

                        editProfileViewModel.updateImages(it1)
                    }
                }

            }
            Log.i("TAG",bitmaps.toString())


            bindind.addImageButton.isClickable = true
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivityForResultProductImages
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindind = FragmentEditProductBinding.bind(view)
        setUpToolbar()
        setCategoriesButton()
        setOnClickListenerForAddImageButton()
        setOnClickListenerForPostBtn()
        setObserve()
    }

    private fun setUpToolbar() {
        val toolbar = bindind.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    private fun setCategoriesButton() {
        bindind.categoriesDropdown.setAdapter(
            ArrayAdapter(
                requireContext(), R.layout.textview, R.id.text, ProductType.entries.toTypedArray()
            )
        )
        Log.i("tag", "setCategoriesButton")
    }

    private fun setOnClickListenerForAddImageButton() {
        bindind.addImageButton.setOnClickListener {
            bindind.addImageButton.isClickable = false
            startActivityForResultProductImages.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )

        }
    }

    private fun setOnClickListenerForPostBtn() {
        bindind.postBtn.setOnClickListener {
            val title = bindind.titleEditText.text.toString().trim()
            val description = bindind.descriptionEditText.text.toString().trim()
            val price = bindind.priceEditText.text.toString()
            val category = bindind.categoriesDropdown.text.toString().trim()
            val location = bindind.locationEditText.text.toString().trim()
            var isValid = true

            if (title == "") {
                bindind.titleEditTextLayout.error = "title should be not empty"
                isValid = false
            } else {
                bindind.titleEditTextLayout.error = null
            }
            if (description == "") {
                bindind.descriptionEditTextLayout.error = "description should be not empty"
                isValid = false
            } else {
                bindind.descriptionEditTextLayout.error = null
            }
            if (price == "") {
                bindind.priceEditTextLayout.error = "price should be not empty"
                isValid = false
            } else {
                bindind.priceEditTextLayout.error = null
            }
            if (category == "") {
                bindind.categoriesDropdownLayout.error = "categories not should be empty"
                isValid = false
            } else {
                bindind.categoriesDropdownLayout.error = null
            }
            if (location == "") {
                bindind.locationEditTextLayout.error = "location should not be empty "
                isValid = false
            } else {
                bindind.locationEditTextLayout.error = null
            }

            if (isValid) {
                requireContext().getSharedPreferences(
                    "mySharePref",
                    AppCompatActivity.MODE_PRIVATE
                ).getString("userId", "-1")?.let { it1 ->
                    editProfileViewModel.postProduct(
                        title,
                        description,
                        price.toDouble(),
                        category,
                        location,
                        it1.toLong()
                    )
                }
            }
        }
    }

    private fun setObserve() {
        //check if post is upload success
        editProfileViewModel.isUpload.observe(viewLifecycleOwner) {
            if (it) {
                parentFragmentManager.popBackStack()
                Toast.makeText(
                    requireContext(),
                    "Post Success upload", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "Unable to post", Toast.LENGTH_SHORT).show()
            }
        }


        //add image
        editProfileViewModel.images.observe(viewLifecycleOwner) {

            Log.i("TAG", "ob ${it.size}")
            if (it.size != 0) {
                bindind.viewPager.adapter = ImageViewAdapter(it)
                bindind.viewPager.visibility = View.VISIBLE
            }
            if (it.size == 10) {
                bindind.addImageButton.visibility = View.GONE
            }
        }

    }
}

package com.application.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.R
import com.application.adapter.ImageViewAdapter
import com.application.callbacks.RemoveDataFromAdapterCallBack
import com.application.callbacks.ProductImageViewBackgroundFragmentCallBack
import com.application.databinding.FragmentEditProductBinding
import com.application.model.ProductType
import com.application.viewmodels.EditProductViewModel
import com.application.viewmodels.ProductDetailViewModel

class EditProductFragment : Fragment(R.layout.fragment_edit_product), RemoveDataFromAdapterCallBack,
    ProductImageViewBackgroundFragmentCallBack {

    val productId: Long?
        get() {
            return  arguments?.getLong(PRODUCT_ID_KEY)
        }

    private lateinit var binding: FragmentEditProductBinding
    private val productDetailViewModel: ProductDetailViewModel by activityViewModels {
        ProductDetailViewModel.FACTORY
    }
    private val editProductViewModel: EditProductViewModel by viewModels {
        EditProductViewModel.FACTORY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(productId == -1L){
            productDetailViewModel.clearProduct()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProductBinding.bind(view)
        productDetailViewModel.product.value?.let {
            editProductViewModel.setProduct(it)
            binding.viewmodel = editProductViewModel
            binding.postBtn.text = "Re-post"
        }
        setUpToolbar()
        setCategoriesButton()
        setOnClickListenerForAddImageButton()
        setOnClickListenerForPostBtn()
        setObserve()
        Log.i("adapter check", editProductViewModel.images.value?.size.toString())
    }

    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setCategoriesButton() {
        binding.categoriesDropdown.setAdapter(
            ArrayAdapter(
                requireContext(), R.layout.textview, R.id.text, ProductType.values().map { it.name }
            )
        )
        Log.i("Tag", "setCategoriesButton")
    }

    private fun setOnClickListenerForAddImageButton() {
        binding.addImageButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                add(R.id.main_view_container, ProductImageViewBackgroundFragment().apply {
                    callBack = this@EditProductFragment
                })
                addToBackStack("background")
                commit()
            }
        }
    }

    private fun setOnClickListenerForPostBtn() {
        binding.postBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val description = binding.descriptionEditText.text.toString().trim()
            val price = binding.priceEditText.text.toString()
            val category = binding.categoriesDropdown.text.toString().trim()
            val location = binding.locationEditText.text.toString().trim()
            var isValid = true

            if (title.isEmpty()) {
                binding.titleEditTextLayout.error = "Title should not be empty"
                isValid = false
            } else {
                binding.titleEditTextLayout.error = null
            }
            if (description.isEmpty()) {
                binding.descriptionEditTextLayout.error = "Description should not be empty"
                isValid = false
            } else {
                binding.descriptionEditTextLayout.error = null
            }
            if (price.isEmpty()) {
                binding.priceEditTextLayout.error = "Price should not be empty"
                isValid = false
            } else {
                binding.priceEditTextLayout.error = null
            }
            if (category.isEmpty()) {
                binding.categoriesDropdownLayout.error = "Category should not be empty"

                isValid = false
            } else {
                binding.categoriesDropdownLayout.error = null
            }
            if( ProductType.stringToProductType(category) == null){
                binding.categoriesDropdownLayout.error = "Please select the correct category"
                isValid = false
            }else{
                binding.categoriesDropdownLayout.error = null
            }
            if (location.isEmpty()) {
                binding.locationEditTextLayout.error = "Location should not be empty"
                isValid = false
            } else {
                binding.locationEditTextLayout.error = null
            }
            if (editProductViewModel.images.value!!.size == 0) {
                binding.textinputError.text = "Must upload a single Image"
                binding.textinputError.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.textinputError.visibility = View.GONE
            }

            if (isValid) {
                requireContext().getSharedPreferences(
                    "mySharePref",
                    AppCompatActivity.MODE_PRIVATE
                ).getString("userId", "-1")?.let { userId ->
                    editProductViewModel.postProduct(
                        title,
                        description,
                        price.toDouble(),
                        category,
                        location,
                        userId.toLong()
                    )
                }
            }
        }
    }

    private fun setObserve() {
        // Check if post is uploaded successfully
        editProductViewModel.isUpload.observe(viewLifecycleOwner) { isUploaded ->
            if (isUploaded) {
                parentFragmentManager.popBackStack()
                Toast.makeText(
                    requireContext(),
                    "${binding.postBtn.text} product successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "Unable to ${binding.postBtn.text}", Toast.LENGTH_SHORT).show()
            }
        }

        // Add image
        editProductViewModel.images.observe(viewLifecycleOwner) { images ->
            Log.i("TAG", "Observe: ${images.size}")
            var count = 5 - images.size
            binding.viewPager.adapter = ImageViewAdapter(images).apply {
                callBack = this@EditProductFragment
            }
            binding.viewPager.visibility = if (images.isNotEmpty()) View.VISIBLE else View.GONE
            binding.addImageButton.visibility = if (images.size >= 5) View.GONE else View.VISIBLE
        }
    }

    override fun removeDataFromList(position: Int) {
        editProductViewModel.removeImage(position)
    }


    override fun getCountOfBitmapList(): Int {
        return editProductViewModel.images.value!!.size
    }

    override fun setBitmap(bitmap: Bitmap) {
        editProductViewModel.updateImage(bitmap)
    }

    companion object {
        val PRODUCT_ID_KEY = "productId"

        fun getInstant(productId: Long): EditProductFragment {
            return EditProductFragment().apply {
                arguments = Bundle().apply {
                    putLong(PRODUCT_ID_KEY, productId)
                }
            }
        }

    }
}

package com.application.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.application.R
import com.application.adapter.ImageViewAdapter
import com.application.callbacks.ImageAdapterListener
import com.application.callbacks.PhotoPickerBottomSheet
import com.application.databinding.FragmentEditProductBinding
import com.application.helper.StringConverter
import com.application.helper.Utility
import com.application.helper.Validator
import com.application.model.ProductType
import com.application.viewmodels.EditProductViewModel
import com.application.viewmodels.ProductViewModel

class EditProductFragment : Fragment(R.layout.fragment_edit_product), ImageAdapterListener,
    PhotoPickerBottomSheet {

    val adapter = ImageViewAdapter(mutableListOf()).apply {
        callBack = this@EditProductFragment
    }
    val productId: Long?
        get() {
            return arguments?.getLong(PRODUCT_ID_KEY)
        }
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    private lateinit var binding: FragmentEditProductBinding
    private val productViewModel: ProductViewModel by activityViewModels {
        ProductViewModel.FACTORY
    }
    private val editProductViewModel: EditProductViewModel by viewModels {
        EditProductViewModel.FACTORY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (productId == -1L) {
            productViewModel.clearProduct()
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProductBinding.bind(view)

        if (productViewModel.product.value == null) {
            binding.toolbar.title = "Add product"
        } else {
            productViewModel.product.value?.let {
                if (savedInstanceState == null) {
                    editProductViewModel.setProduct(it)
                }
                binding.toolbar.menu.findItem(R.id.post).setIcon(R.drawable.baseline_check_24)
                binding.toolbar.title = "Edit product"
            }
        }
        if (savedInstanceState == null) {
            setObserveForUI()
        }
        val textError = savedInstanceState?.getString("error", "")
        if (textError?.isNotEmpty() == true) {
            binding.textinputError.text = textError
            binding.textinputError.visibility = View.VISIBLE
        } else {
            binding.textinputError.visibility = View.GONE
        }
        setObserve()
        setUpToolbar()
        setCategoriesButton()
        setOnClickListenerForAddImageButton()
        setOnClickListenerForPostBtn()
        setUpOnBackPress()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("error", binding.textinputError.text.toString())
        super.onSaveInstanceState(outState)
    }

    private fun isDataUpdate(): Boolean {
        val product = editProductViewModel.product.value
        return isChanged(product?.title, binding.titleEditText.text.toString()) ||
                isChanged(product?.description, binding.descriptionEditText.text.toString()) ||
                isChanged(product?.price, binding.priceEditText.text.toString().toDoubleOrNull()) ||
                isChanged(
                    ProductType.productTypeToString(product?.productType),
                    binding.categoriesDropdown.text.toString()
                ) ||
                isChanged(product?.location, binding.locationEditText.text.toString()) ||
                isChanged(product?.images?.size, editProductViewModel.images.value?.size)
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

    private fun onBackPress() {

        Log.i("EditProductFragment", "onBackPress ${editProductViewModel.isDataUpdate}")
        if (isDataUpdate()) {
            AlertDialog.Builder(context).apply {
                setMessage("If you go back, any changes you made will be lost")
                setPositiveButton("Confirm") { _, _ ->
                    parentFragmentManager.popBackStack()
                }
                setNegativeButton("NO", null)
                show()
            }
        } else {
            parentFragmentManager.popBackStack()
        }
    }


    private fun setObserveForUI() {
        productViewModel.product.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.titleEditText.text = StringConverter.toEditable(it.title)
                binding.descriptionEditText.text = StringConverter.toEditable(it.description)
                binding.priceEditText.text =
                    StringConverter.toEditable(Utility.convertToString(it.price))
                binding.categoriesDropdown.text =
                    StringConverter.toEditable(ProductType.productTypeToString(it.productType))
                binding.locationEditText.text = StringConverter.toEditable(it.location)
            }
        }
    }

    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            onBackPress()
        }
    }

    private fun setUpIndicatorForViewPager(imageSize: Int) {

        if (imageSize != -1) {
            val slideDot = binding.indicator
            slideDot.removeAllViews()
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 8, 0)
                gravity = Gravity.CENTER
            }

            val dotsImage = Array(imageSize) { ImageView(requireContext()) }
            dotsImage.forEach { img ->
                img.setImageResource(
                    R.drawable.non_active_dot
                )
                slideDot.addView(img, params)
            }
            if (dotsImage.isNotEmpty()) {
                if (binding.viewPager.currentItem < dotsImage.size) {
                    dotsImage[binding.viewPager.currentItem].setImageResource(R.drawable.active_dot)
                } else {
                    dotsImage[binding.viewPager.currentItem - 1].setImageResource(R.drawable.active_dot)
                }
            }
            pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    dotsImage.mapIndexed { index, imageView ->
                        if (position == index) {
                            imageView.setImageResource(
                                R.drawable.active_dot
                            )
                        } else {
                            imageView.setImageResource(R.drawable.non_active_dot)
                        }
                    }
                    super.onPageSelected(position)
                }
            }
            binding.viewPager.registerOnPageChangeCallback(
                pageChangeListener
            )
        }
    }

    private fun setCategoriesButton() {
        binding.categoriesDropdown.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.textview,
                R.id.text,
                ProductType.entries.map { ProductType.productTypeToString(it) }
            )
        )
    }

    private fun setOnClickListenerForAddImageButton() {
        binding.addImageButton.setOnClickListener {
            if (childFragmentManager.findFragmentByTag("bottomSheet") == null) {
                val bottomSheet = BottomSheetDialogPhotoPicker()
                bottomSheet.show(childFragmentManager, "bottomSheet")
            }
        }
    }

    private fun setOnClickListenerForPostBtn() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.post -> {
                    val title = binding.titleEditText.text.toString().trim()
                    val description = binding.descriptionEditText.text.toString().trim()
                    val price = binding.priceEditText.text.toString()
                    val category = binding.categoriesDropdown.text.toString().trim()
                    val location = binding.locationEditText.text.toString().trim()
                    var isValid = true



                    Validator.validateField(
                        location
                    ) {
                        if (!it) {
                            isValid = it
                            binding.locationEditTextLayout.error = "Location should not be empty"
                            binding.locationEditTextLayout.requestFocus()
                        } else {
                            binding.locationEditTextLayout.error = null
                        }
                    }

                    Validator.validatePrice(price) { _isValid, errorMessage ->
                        if (!_isValid) {
                            isValid = _isValid
                            binding.priceEditTextLayout.error = errorMessage
                            binding.priceEditTextLayout.requestFocus()
                        } else {
                            binding.priceEditTextLayout.error = errorMessage
                        }
                    }

                    Validator.validateCategory(category) {
                        if (!it) {
                            isValid = it
                            binding.categoriesDropdownLayout.error =
                                "Please select the correct category"
                            binding.categoriesDropdownLayout.requestFocus()
                        } else {
                            binding.categoriesDropdownLayout.error = null
                        }
                    }

                    Validator.validateField(
                        description,
                    ) {
                        if (!it) {
                            isValid = it
                            binding.descriptionEditTextLayout.error =
                                "Description should not be empty"
                            binding.descriptionEditTextLayout.requestFocus()
                        } else {
                            binding.descriptionEditTextLayout.error = null
                        }
                    }

                    Validator.validateField(
                        title
                    ) {
                        if (!it) {
                            isValid = it
                            binding.titleEditTextLayout.error = "Title should not be empty"
                            binding.titleEditTextLayout.requestFocus()
                        } else {
                            binding.titleEditTextLayout.error = null
                        }
                    }

                    Validator.validateImages(
                        editProductViewModel.images.value!!.size,
                        binding.textinputError
                    ) {
                        if (!it) {
                            isValid = it
                            binding.nestedScrollView2.scrollTo(0, 0)
                            binding.textinputError.visibility = View.VISIBLE
                        } else {
                            binding.textinputError.visibility = View.GONE
                        }
                    }

                    if (isValid) {
                        editProductViewModel.postProduct(
                            title,
                            description,
                            price.toDouble(),
                            category,
                            location,
                            Utility.getLoginUserId(requireContext())
                        )
                    }
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    return@setOnMenuItemClickListener false
                }
            }

        }
    }

    private fun setObserve() {
        // Check if post is uploaded successfully
        editProductViewModel.isUpload.observe(viewLifecycleOwner) { isUploaded ->
            if (isUploaded == true) {
                //Temp Change Join Product Details ViewModel and Product EditViewModel
                productViewModel.fetchProductDetailsUsingProductId(
                    productId!!,
                    Utility.getLoginUserId(requireContext())
                )
                val message = if (productViewModel.product.value == null) {
                    "Post product successfully"
                } else {
                    "update product successfully"
                }
                parentFragmentManager.popBackStack()
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (isUploaded == false) {
                Toast.makeText(
                    requireContext(),
                    "Unable to Post",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.viewPager.adapter = adapter

        editProductViewModel.images.observe(viewLifecycleOwner) { images ->

            if (productViewModel.product.value?.images?.size != images?.size) {

                if (productViewModel.product.value?.images?.size == null && images?.size == 0
                    && !editProductViewModel.isDataUpdate
                ) {
                    editProductViewModel.isDataUpdate = false
                } else {
                    editProductViewModel.isDataUpdate = true
                }
            }
            val _images = mutableListOf<Bitmap>()
            images?.forEach {
                _images.add(it.copy(it.config, true))
            }
            adapter.data = _images
            adapter.notifyDataSetChanged()
            setUpIndicatorForViewPager(_images.size)
            binding.viewPager.visibility = if (_images.isNotEmpty()) View.VISIBLE else View.GONE
            binding.addImageButton.visibility = if (_images.size >= 5) View.GONE else View.VISIBLE
        }
    }


    override fun onRemoveButtonClick(element: Bitmap) {
        Log.i("EditProductFragment", "position ${adapter.data.indexOf(element)}")
        editProductViewModel.removeImage(adapter.data.indexOf(element))
    }


    override fun getBitmapCount(): Int {
        return MAX_IMAGE_SIZE - editProductViewModel.images.value!!.size
    }

    override fun addBitmap(bitmap: Bitmap) {
        editProductViewModel.updateImage(bitmap)
    }


    companion object {
        const val PRODUCT_ID_KEY = "productId"
        const val MAX_IMAGE_SIZE = 5
        fun getInstant(productId: Long): EditProductFragment {
            return EditProductFragment().apply {
                arguments = Bundle().apply {
                    putLong(PRODUCT_ID_KEY, productId)
                }
            }
        }

    }
}

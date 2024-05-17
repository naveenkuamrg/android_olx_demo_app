package com.application.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

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

        val textError = savedInstanceState?.getString("error", "")
        if (textError?.isNotEmpty() == true) {
            binding.textinputError.text = textError
            binding.textinputError.visibility = View.VISIBLE
        } else {
            binding.textinputError.visibility = View.GONE
        }

        val productId = savedInstanceState?.getLong("productId")
        if (productId != null) {
            if (productId != -1L) {

                productViewModel.fetchProductDetailsUsingProductId(
                    productId,
                )
            } else {
                productViewModel.clearProduct()
            }
        }
        if (savedInstanceState == null) {
            setObserveForUI()
        }
//        Utility.removeErrorAfterTextChanged(binding.titleEditText, binding.titleEditTextLayout)
//        Utility.removeErrorAfterTextChanged(binding.descriptionEditText, binding.descriptionEditTextLayout)
//        Utility.removeErrorAfterTextChanged(binding.priceEditText, binding.priceEditTextLayout)
//        Utility.removeErrorAfterTextChanged(binding.locationEditText, binding.locationEditTextLayout)
        setObserve()
        setUpToolbar()
        setCategoriesButton()
        setOnClickListenerForAddImageButton()
        setOnClickListenerForPostBtn()
        setUpOnBackPress()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.i("saveRestore", "restore")

        binding.categoriesDropdown.text = SpannableStringBuilder("")
        binding.categoriesDropdown.setText(
            savedInstanceState?.getString(
                "Categories", ""
            ), false
        )
//        val imageSize = savedInstanceState?.getInt("tempImageSize")
//        if (imageSize != null) {
//            editProductViewModel.setTempImages(restoreBitmapFromFile(imageSize))
//        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("error", binding.textinputError.text.toString())
        outState.putString("Categories", binding.categoriesDropdown.text.toString())
        outState.putLong("productId", productViewModel.product.value?.id ?: -1L)
//        val tempsImages = editProductViewModel.images.value
//        if (tempsImages != null) {
//            saveBitmapToFile(tempsImages)
//            outState.putInt("tempImageSize", tempsImages.size)
//        }
    }


    private fun isDataUpdate(): Boolean {
        val product = productViewModel.product.value
        Log.i("images","${product?.images.toString()}  ${editProductViewModel.images.value}")
        return isChanged(product?.title, binding.titleEditText.text.toString()) ||
                isChanged(product?.description, binding.descriptionEditText.text.toString()) ||
                isChanged(product?.price, binding.priceEditText.text.toString().toDoubleOrNull()) ||
                isChanged(
                    ProductType.productTypeToString(product?.productType),
                    binding.categoriesDropdown.text.toString()
                ) ||
                isChanged(product?.location, binding.locationEditText.text.toString()) ||
                isChanged(product?.images?: emptyList(), editProductViewModel.images.value)
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

                editProductViewModel.setTempImages(it.images)
                binding.titleEditText.text = StringConverter.toEditable(it.title)
                binding.descriptionEditText.text = StringConverter.toEditable(it.description)
                binding.priceEditText.text =
                    StringConverter.toEditable(Utility.convertToString(it.price))
                binding.categoriesDropdown.setText(
                    StringConverter.toEditable(ProductType.productTypeToString(it.productType)),
                    false
                )
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

        productViewModel.product.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.toolbar.title = "Add product"
            } else {
//                editProductViewModel.setProduct(it)
                productViewModel.product.value?.let {
                    binding.toolbar.menu.findItem(R.id.post).setIcon(R.drawable.baseline_check_24)
                    binding.toolbar.title = "Edit product"
                }
            }
        }
    }

    private fun setUpIndicatorForViewPager(imageSize: Int) {

        if (imageSize != -1 && imageSize != 1) {
            binding.indicator.visibility = View.VISIBLE
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
        } else {
            binding.indicator.visibility = View.GONE
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

        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.priceEditText.setOnEditorActionListener { _, _, _ ->
            binding.categoriesDropdown.requestFocus()
            binding.categoriesDropdown.showDropDown()
            inputMethodManager.hideSoftInputFromWindow(binding.priceEditText.windowToken, 0)
            return@setOnEditorActionListener true
        }
        binding.categoriesDropdown.setOnFocusChangeListener { view, b ->
            inputMethodManager.hideSoftInputFromWindow(binding.priceEditText.windowToken, 0)
        }

        binding.categoriesDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
//                binding.categoriesDropdownLayout.error = ""
                binding.editDetailsContainer.requestFocus(R.id.location_edit_text)
                inputMethodManager.showSoftInput(
                    binding.locationEditText,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }


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
                    Validator.validateCategory(category) {
                        if (!it) {
                            isValid = it
                            binding.categoriesDropdownLayout.error =
                                "Please select the correct category"
//                            binding.categoriesDropdown.showDropDown()
                        } else {
                            binding.categoriesDropdownLayout.error = null
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
                            productViewModel.product.value?.id,
                            title,
                            description,
                            productViewModel.product.value?.postedDate ?: Date().time,
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
                    productId!!
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
//                binding.textinputError.visibility = View.GONE
                editProductViewModel.isDataUpdate =
                    !(productViewModel.product.value?.images?.size == null && images?.size == 0
                            && !editProductViewModel.isDataUpdate)
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

    private fun saveBitmapToFile(bitmaps: List<Bitmap>) {
        bitmaps.forEachIndexed { index, bitmap ->
            val file = File("${requireContext().cacheDir}", "product${index}.png")
            lifecycleScope.launch(Dispatchers.IO) {
                file.outputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            }
        }
    }



    // Restoring Bitmap from file
    private fun restoreBitmapFromFile(size: Int): List<Bitmap> {
        val images = mutableListOf<Bitmap>()
        for (i in 0..<size) {
            lifecycleScope.launch {
                val filePath = requireContext().cacheDir.toString() + "/product${i}.png"
                val file = File(filePath)
                if (file.exists()) {
                    images.add(BitmapFactory.decodeFile(filePath))
                }
            }
        }

        return images
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

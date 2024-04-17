package com.application.fragments

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
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

        if (savedInstanceState == null) {
            productViewModel.product.value?.let {
                editProductViewModel.setProduct(it)
                binding.postBtn.text = "Re-post"
                binding.toolbar.title = "Edit product"
            }
        }
        if (productViewModel.product.value == null) {
            binding.toolbar.title = "Add product"
        }
        if (savedInstanceState == null) {
            setObserveForUI()
        }

        setUpToolbar()
        setCategoriesButton()
        setOnClickListenerForAddImageButton()
        setOnClickListenerForPostBtn()
        setObserve()
        setUpOnBackPress()
        setTextChangedListener()
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

    private fun onBackPress(){
        if (editProductViewModel.isDataUpdate) {
            AlertDialog.Builder(context).apply {
                setMessage("If you go back, any changes you made will be lost")
                setPositiveButton("OK") { _, _ ->
                    editProductViewModel.isDataUpdate = false
                    parentFragmentManager.popBackStack()
                }
                setNegativeButton("NO", null)
                show()
            }
        } else {
            parentFragmentManager.popBackStack()
        }
    }
    private fun setTextChangedListener() {
        val product = productViewModel.product.value
        updateIsDataUpdate(binding.titleEditText, product?.title)
        updateIsDataUpdate(binding.descriptionEditText, product?.description)
        if(product?.productType != null) {
            updateIsDataUpdate(
                binding.descriptionEditText,
                ProductType.productTypeToString(product.productType)
            )
        }
        updateIsDataUpdate(binding.locationEditText, product?.location)
        updateIsDataUpdate(binding.priceEditText, product?.price.toString())
    }

    private fun updateIsDataUpdate(editText: EditText, previousData: String?) {
        editText.addTextChangedListener {
            if (it.toString() == previousData) {
                editProductViewModel.isDataUpdate = false
                return@addTextChangedListener
            }
            editProductViewModel.isDataUpdate = true
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
            if (ProductType.stringToProductType(category) == null) {
                binding.categoriesDropdownLayout.error = "Please select the correct category"
                isValid = false
            } else {
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
                editProductViewModel.postProduct(
                    title,
                    description,
                    price.toDouble(),
                    category,
                    location,
                    Utility.getLoginUserId(requireContext())
                )

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
                //
                parentFragmentManager.popBackStack()
                Toast.makeText(
                    requireContext(),
                    "${binding.postBtn.text} product successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (isUploaded == false) {
                Toast.makeText(
                    requireContext(),
                    "Unable to ${binding.postBtn.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.viewPager.adapter = adapter

        editProductViewModel.images.observe(viewLifecycleOwner) { images ->
            if (productViewModel.product.value?.images?.size != images?.size) {
                editProductViewModel.isDataUpdate = true
                Log.i("TAg new", "${productViewModel.product.value?.images?.size}")
                if (productViewModel.product.value?.images?.size == null && images?.size == 0) {
                    editProductViewModel.isDataUpdate = false
                }
            }
            val _images = mutableListOf<Bitmap>()
            images?.forEach {
                _images.add(it.copy(it.config, true))
            }
            adapter.data = _images
            setUpIndicatorForViewPager(_images.size)
            binding.viewPager.visibility = if (_images.isNotEmpty()) View.VISIBLE else View.GONE
            binding.addImageButton.visibility = if (_images.size >= 5) View.GONE else View.VISIBLE
        }
    }


    override fun onRemoveButtonClick(position: Int) {
        editProductViewModel.removeImage(position)
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

package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.application.R
import com.application.adapter.ImageViewAdapter
import com.application.adapter.ProfileSummaryAdapter
import com.application.databinding.FragmentProductDetailsBinding
import com.application.helper.Utility
import com.application.model.AvailabilityStatus
import com.application.viewmodels.ProductViewModel


class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    val viewModel: ProductViewModel by activityViewModels { ProductViewModel.FACTORY }

    lateinit var binding: FragmentProductDetailsBinding
    private val isCurrentUserProduct: Boolean
        get() {
            return viewModel.product.value?.sellerId == Utility.getLoginUserId(requireContext())
        }

    private var userId: Long = -1

    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userId = Utility.getLoginUserId(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.clearLiveData()
            viewModel.clearProduct()
            val currentProductId = arguments?.getLong("currentProductId")
            val notificationId = arguments?.getLong("notificationId")
            if (currentProductId != null && currentProductId != 0L) {
                viewModel.fetchProductDetailsUsingProductId(currentProductId, userId)
            }
            if (notificationId != null && notificationId != 0L) {
                viewModel.fetchProductDetailsUsingNotificationId(notificationId, userId)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductDetailsBinding.bind(view)
        setObserve()
        setOnClickListener()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        val recyclerView = binding.productDetailLayout.profileRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        recyclerView.adapter =
            ProfileSummaryAdapter(requireContext()) { userId ->
                viewModel.updateIsContented(userId, viewModel.product.value?.id!!)
            }
    }

    private fun setView() {

        if (
            viewModel.product.value?.availabilityStatus == AvailabilityStatus.SOLD_OUT
        ) {
            binding.productDetailLayout.buttonContainer.visibility = View.GONE
            binding.productDetailLayout.profileRecyclerViewContainer.visibility = View.GONE
            binding.toolbar.menu.removeItem(R.id.edit)

        } else {
            binding.productDetailLayout.buttonContainer.visibility = View.VISIBLE
            binding.productDetailLayout.profileRecyclerViewContainer.visibility = View.VISIBLE
            binding.productDetailLayout.favouriteImg.visibility = View.GONE
        }

        if (!isCurrentUserProduct) {
            binding.productDetailLayout.markAsSoldButton.visibility = View.GONE
            binding.productDetailLayout.imInterestedBtn.visibility = View.VISIBLE
            binding.productDetailLayout.profileRecyclerViewContainer.visibility = View.GONE
            if (
                viewModel.product.value?.availabilityStatus == AvailabilityStatus.SOLD_OUT
            ) {
                binding.productDetailLayout.favouriteImg.visibility = View.GONE
            } else {
                binding.productDetailLayout.favouriteImg.visibility = View.VISIBLE
            }

        } else {
            binding.productDetailLayout.markAsSoldButton.visibility = View.VISIBLE

            binding.productDetailLayout.favouriteImg.visibility = View.GONE
        }


    }


    private fun setOnClickListener() {
        binding.productDetailLayout.markAsSoldButton.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setMessage(
                    "Are you sure you want to change the status of this product to 'Sold'?" +
                            " This action cannot be reverted."
                )
                setPositiveButton("OK") { _, _ ->
                    viewModel.updateMarkAsSold()
                }
                setNegativeButton("NO") { _, _ -> }
                show()
            }
        }

        binding.productDetailLayout.imInterestedBtn.setOnClickListener { btn ->
            btn.isEnabled = false
            val message = if (viewModel.product.value?.isInterested == false) {
                "Your contact is shared with the product seller."
            } else {
                "Your contact is removed from the product seller."
            }

            AlertDialog.Builder(requireContext()).apply {
                setMessage(message)
                setPositiveButton("Confirm") { _, _ ->
                    viewModel.product.value?.let {
                        viewModel.updateProductInterested(it, userId, !it.isInterested)
                    }
                }
                setNegativeButton("No") { _, _ -> btn.isEnabled = true }
                setCancelable(false)
                show()
            }


        }
        binding.productDetailLayout.favouriteImg.setOnClickListener {
            viewModel.updateIsInterested(userId)
        }
    }

    private fun setObserve() {

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.appBarLayout4.visibility = View.GONE
                binding.productDetailLayout.productDetailLayout.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
            }

            if (it == false) {
                binding.appBarLayout4.visibility = View.VISIBLE
                binding.productDetailLayout.productDetailLayout.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
                setView()
                setUpToolbar()
            }
        }

        viewModel.product.observe(viewLifecycleOwner) {
            if (isCurrentUserProduct && it?.availabilityStatus != AvailabilityStatus.SOLD_OUT) {
                if (it?.id != null) {
                    viewModel.fetchProfileList(it.id)
                }
            }
            binding.productDetailLayout.viewModel = viewModel

            setUpIndicatorForViewPager(it?.images?.size ?: -1)

            binding.productDetailLayout.viewPager.adapter =
                (viewModel.product.value?.images)?.toMutableList()
                    ?.let { it1 -> ImageViewAdapter(it1) }
            setView()
            updateButtonUI()
        }


        viewModel.isDelete.observe(viewLifecycleOwner) {
            if (it == true) {
                Utility.showToast(requireContext(), "Product delete Successfully")
                parentFragmentManager.popBackStack()
                viewModel.clearProduct()
            }
            if (it == false) {
                Utility.showToast(
                    requireContext(),
                    "Product delete un-successfully please try again"
                )
            }
        }

        viewModel.isInterestedChangeIsUpdate.observe(viewLifecycleOwner) {

            it?.let { isUpdate ->
                binding.productDetailLayout.imInterestedBtn.isEnabled = true
                if (isUpdate) {
                    Log.i("check ", viewModel.product.value?.isInterested!!.toString())
                    viewModel.setProductInterestedValue(!viewModel.product.value?.isInterested!!)
                    updateButtonUI()
                }
                if (!isUpdate) {
                    Utility.showToast(
                        requireContext(),
                        "Sorry, your update was not successful. Please try again later"
                    )
                }

            }
        }

        viewModel.profileList.observe(viewLifecycleOwner) {
            binding.productDetailLayout.noData.imageView2.visibility = View.GONE
            if (it.isEmpty()) {
                binding.productDetailLayout.noData.noDataLayout.visibility = View.VISIBLE
                binding.productDetailLayout.noData.errorText.textSize = 16F
                binding.productDetailLayout.noData.errorText.text =
                    "Oops! It seems like your products are still waiting to steal some hearts"
            } else {
                binding.productDetailLayout.noData.noDataLayout.visibility = View.GONE
            }
            (binding.productDetailLayout.profileRecyclerView.adapter as ProfileSummaryAdapter)
                .setData(
                    it
                )

        }

        viewModel.isWishListIsUpdate.observe(viewLifecycleOwner) {
            if (viewModel.product.value?.isWishList == true) {
                binding.productDetailLayout.favouriteImg.setImageResource(
                    R.drawable.ic_favorite_fill
                )
            }

            if (viewModel.product.value?.isWishList == false) {
                binding.productDetailLayout.favouriteImg.setImageResource(
                    R.drawable.ic_favorite_outline
                )
            }
        }

        viewModel.exception.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.productDetailLayoutContainer.visibility = View.GONE
                binding.noData.noDataLayout.visibility = View.VISIBLE
                binding.noData.errorText.text = "This Product Deleted by seller"
                binding.toolbar.menu.removeItem(R.id.edit)
                binding.toolbar.menu.removeItem(R.id.delete)
            }
        }

    }

    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        if (isCurrentUserProduct) {

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        if (viewModel.product.value?.availabilityStatus == AvailabilityStatus
                                .SOLD_OUT
                        ) {
                            AlertDialog.Builder(requireContext()).apply {
                                setMessage("This product is sold out, so you can't edit.")
                                setPositiveButton("OK") { _, _ -> }
                                show()
                            }
                            return@setOnMenuItemClickListener false
                        }

                        parentFragmentManager.beginTransaction().apply {
                            replace(
                                R.id.main_view_container,
                                EditProductFragment.getInstant(viewModel.product.value?.id!!)
                            )
                            addToBackStack("editProductFragment")
                            commit()
                        }
                        return@setOnMenuItemClickListener false
                    }

                    R.id.delete -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setMessage("Are you sure to delete product")
                            setPositiveButton("Yes") { _, _ ->
                                viewModel.removeProductDetail()
                                arguments?.putLong("currentProductId", -1)
                            }
                            setNegativeButton("No") { _, _ -> }
                            show()
                        }

                    }

                }

                return@setOnMenuItemClickListener true
            }
        } else {
            toolbar.menu.removeItem(R.id.edit)
            toolbar.menu.removeItem(R.id.delete)
        }
    }

    private fun updateButtonUI() {

        if (viewModel.product.value?.isInterested == false) {
            binding.productDetailLayout.imInterestedBtn.text = "I'm Interested"
        } else {
            binding.productDetailLayout.imInterestedBtn.text = "remove from Interested"
        }
    }

    private fun setUpIndicatorForViewPager(imageSize: Int) {

        if (imageSize != -1 && imageSize != 1) {
            val slideDot = binding.productDetailLayout.indicator
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
            binding.productDetailLayout.viewPager.registerOnPageChangeCallback(
                pageChangeListener
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::pageChangeListener.isInitialized) {
            binding.productDetailLayout.viewPager.unregisterOnPageChangeCallback(pageChangeListener)
        }
    }

}
package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ImageViewAdapter
import com.application.adapter.ProfileSummaryAdapter
import com.application.databinding.FragmentProductDetailsBinding
import com.application.helper.Utility
import com.application.model.AvailabilityStatus
import com.application.viewmodels.ProductDetailViewModel

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    val viewModel: ProductDetailViewModel by activityViewModels { ProductDetailViewModel.FACTORY }
    lateinit var binding: FragmentProductDetailsBinding

    private val isCurrentUserProduct: Boolean
        get() {
            return arguments?.getString("fragment") != "home"
        }

    private val userId: Long by lazy {
        requireContext().getSharedPreferences(
            "mySharePref",
            AppCompatActivity.MODE_PRIVATE
        ).getString("userId", "-1")?.toLong() ?: -1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clearLiveData()
        Log.i("TAG ProductDetailsFragment","viewModel")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var userId = 0L
        requireContext().getSharedPreferences(
            "mySharePref",
            AppCompatActivity.MODE_PRIVATE
        ).getString("userId", "-1")?.let {
            userId = it.toLong()
        }
        super.onViewCreated(view, savedInstanceState)

            viewModel.fetchProductDetails(arguments?.getLong("currentProductId")!!, userId)

        binding = FragmentProductDetailsBinding.bind(view)
        setObserve()
        setUpToolbar()
        setOnClickListener()
        setView()

    }

    private fun setView() {
        if (!isCurrentUserProduct) {
            binding.productDetailLayout.markAsSoldButton.visibility = View.GONE
            binding.productDetailLayout.imInterestedBtn.visibility = View.VISIBLE
            binding.productDetailLayout.profileRecyclerViewContainer.visibility = View.GONE
        } else {
            if (
                viewModel.product.value?.availabilityStatus == AvailabilityStatus.SOLD_OUT
            ) {
                binding.productDetailLayout.buttonContainer.visibility = View.GONE
                binding.productDetailLayout.profileRecyclerViewContainer.visibility = View.GONE

            } else {
                binding.productDetailLayout.buttonContainer.visibility = View.VISIBLE
                binding.productDetailLayout.profileRecyclerViewContainer.visibility = View.VISIBLE

            }
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
            viewModel.product.value?.let {
                viewModel.updateProductInterested(it.id!!, userId, !it.isInterested!!)
            }
        }
    }

    private fun setObserve() {
        //add loader background image should be gone while loading
        viewModel.isLoading.observe(viewLifecycleOwner) {

            Log.i("id ProductDetailsFragment",it.toString())

            if (it != true) {

            }
        }
        viewModel.product.observe(viewLifecycleOwner){
            Log.i("observe","observe")
            if(isCurrentUserProduct && it?.availabilityStatus != AvailabilityStatus.SOLD_OUT) {
                if(it?.id != null) {
                    viewModel.fetchProfileList(it.id)
                }
            }
            binding.productDetailLayout.viewModel = viewModel
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
                    viewModel.product.value?.id?.let { it1 ->
                        viewModel.fetchProductDetails(
                            it1,
                            userId
                        )
                    }
                } else {
                    Utility.showToast(
                        requireContext(),
                        "Sorry, your update was not successful. Please try again later"
                    )
                }

            }
        }

        viewModel.profileList.observe(viewLifecycleOwner){
            Log.i("profileList",it.toString())
            val adapter = ProfileSummaryAdapter(it,requireContext())
            binding.productDetailLayout.profileRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.productDetailLayout.profileRecyclerView.adapter = adapter
        }

    }


    private fun setUpToolbar() {
        Log.i("toolTest", viewModel.product.value?.sellerId.toString())
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        if (isCurrentUserProduct) {
            MenuInflater(requireContext()).inflate(R.menu.product_details_menu, toolbar.menu)
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
                                arguments?.putLong("currentProductId",-1)
                            }
                            setNegativeButton("No") { _, _ -> }
                            show()
                        }

                    }

                }

                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun updateButtonUI() {


            if (viewModel.product.value?.isInterested == false) {
                binding.productDetailLayout.imInterestedBtn.text = "I'm Interested"
            } else {
                binding.productDetailLayout.imInterestedBtn.text = "remove from Interested"
            }



    }


}
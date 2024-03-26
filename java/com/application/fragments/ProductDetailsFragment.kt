package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.R
import com.application.adapter.ImageViewAdapter
import com.application.databinding.FragmentProductDetailsBinding
import com.application.model.AvailabilityStatus
import com.application.viewmodels.ProductDetailViewModel

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    val viewModel: ProductDetailViewModel by activityViewModels { ProductDetailViewModel.FACTORY }
    lateinit var binding: FragmentProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clearIsDelete()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchProductDetails(arguments?.getLong("currentProductId")!!, 1)
        binding = FragmentProductDetailsBinding.bind(view)
        setObserve()
        setUpToolbar()
        setOnClickListener()
    }


    private fun setOnClickListener() {
        binding.markAsSoldButton.setOnClickListener {
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
    }

    private fun setObserve() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if(it == true) {
                binding.productDetailLayout.viewModel = viewModel
                binding.productDetailLayout.viewPager.adapter =
                    (viewModel.product.value?.images)?.toMutableList()
                        ?.let { it1 -> ImageViewAdapter(it1) }
                Log.i("TAG status", viewModel.product.value?.availabilityStatus.toString())
                if (viewModel.product.value?.availabilityStatus == AvailabilityStatus.SOLD_OUT) {
                    binding.markAsSoldButton.visibility = View.GONE
                }
            }
        }
        viewModel.isDelete.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(
                    requireContext(),
                    "Product delete Successfully", Toast.LENGTH_SHORT
                )
                    .show()
                parentFragmentManager.popBackStack()
            }
            if (it == false) {
                Toast.makeText(
                    requireContext(),
                    "Product delete un-successfully please try again", Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    private fun setUpToolbar() {
        Log.i("toolTest", "setUpToolbar")
        val toolbar = binding.toolbar
        MenuInflater(requireContext()).inflate(R.menu.product_details_menu, toolbar.menu)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    if(viewModel.product.value?.availabilityStatus == AvailabilityStatus.SOLD_OUT){
                        AlertDialog.Builder(requireContext()).apply {
                            setMessage("This product is sold out, so you can't edit.")
                            setPositiveButton("OK"){_,_->}
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
                        }
                        setNegativeButton("No") {_,_->}
                        show()
                    }

                }

            }

            return@setOnMenuItemClickListener true
        }
    }


}
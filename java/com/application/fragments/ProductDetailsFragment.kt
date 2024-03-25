package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.compose.ui.input.key.Key.Companion.Menu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.application.R
import com.application.adapter.ImageViewAdapter
import com.application.databinding.FragmentChangePasswordBinding
import com.application.databinding.FragmentProductDetailsBinding
import com.application.viewmodels.ProductDetailViewModel

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    val viewModel: ProductDetailViewModel by activityViewModels { ProductDetailViewModel.FACTORY }
    lateinit var binding: FragmentProductDetailsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewModel.featchProductDetails(arguments?.getLong("currentProductId")!!, 1)
        binding = FragmentProductDetailsBinding.bind(view)
        setObserve()
        setUpToolbar()
    }


    private fun setObserve() {
        Log.i("toolTest", "setObserve")
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.productDetailLayout.viewModel = viewModel
            binding.productDetailLayout.viewPager.adapter =
                (viewModel.product.value?.images)?.toMutableList()
                    ?.let { it1 -> ImageViewAdapter(it1) }
        }
    }

    private fun setUpToolbar() {
        Log.i("toolTest", "setUpToolbar")
        val toolbar = binding.toolbar
        MenuInflater(requireContext()).inflate(R.menu.product_details_menu, toolbar.menu)
        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
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
            }

            return@setOnMenuItemClickListener true
        }
    }
}
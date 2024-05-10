package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.databinding.FragmentFilterProductsBinding
import com.application.helper.Utility.commitWithSlideAnimation
import com.application.model.ProductType
import com.google.android.material.progressindicator.CircularProgressIndicator

class FilterProductListFragment :
    SortableProductListFragment(R.layout.fragment_filter_products) {

    lateinit var binding: FragmentFilterProductsBinding

    override lateinit var progressIndicator: CircularProgressIndicator

    override lateinit var recyclerView: RecyclerView

    private val getFilterType: ProductType?
        get() {
            val type = arguments?.getString("filterType", "")
            return if (type != null) {
                ProductType.stringToProductType(type)
            } else {
                null
            }
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            getFilterType?.let { productListViewModel.getProductSummary(it) }
        }
        binding = FragmentFilterProductsBinding.bind(view)
        recyclerView = binding.recycleView
        progressIndicator = binding.progressCircular
        super.onViewCreated(view, savedInstanceState)
        binding.noData.errorText.text = "Sorry, No product available for this category"

        setUpToolbar()
        setObserve()
    }


    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = ProductType.productTypeToString(getFilterType)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        toolbar.menu.removeItem(R.id.notification)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort -> {
                    if (childFragmentManager.findFragmentByTag("bottomSheet") != null) {
                        return@setOnMenuItemClickListener true
                    }
                    val bottomSheet = BottomSheetDialogSort()
                    bottomSheet.show(childFragmentManager, "bottomSheet")
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

    }


    override fun onProductSummaryClick(productId: Long) {
        parentFragmentManager.commitWithSlideAnimation(
            "showProductDetailFragment",
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(
                        "currentProductId",
                        productId
                    )
                }
            },
            R.id.main_view_container
        )

    }

    override fun isListEmpty(isEmpty: Boolean) {
        Log.i("FilterProductListFragment", "isListEmpty $isEmpty")
        if (isEmpty) {
            binding.noData.noDataLayout.visibility = View.VISIBLE
        } else {
            binding.noData.noDataLayout.visibility = View.GONE
        }
    }


    companion object {
        fun getInstant(type: ProductType): FilterProductListFragment {
            return FilterProductListFragment().apply {
                arguments = Bundle().apply {
                    putString("filterType", ProductType.productTypeToString(type))
                }
            }
        }
    }


}
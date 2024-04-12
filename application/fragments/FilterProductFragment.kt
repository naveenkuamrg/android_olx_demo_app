package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.OnItemClickListener
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentFilterProductsBinding
import com.application.helper.Utility
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.viewmodels.ProductListViewModel

class FilterProductFragment : Fragment(R.layout.fragment_filter_products), OnItemClickListener,
SortBottomSheetCallback{

    lateinit var binding: FragmentFilterProductsBinding

    private val productListViewModel: ProductListViewModel by viewModels { ProductListViewModel.FACTORY }

    private var isSortTypeUpdate = false

    var userId: Long = -1L
    private val getFilterType: ProductType?
        get() {
            val type = arguments?.getString("filterType", "")
            return if (type != null) {
                ProductType.stringToProductType(type)
            } else {
                null
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userId = Utility.getLoginUserId(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.product_recycle_view, ProductRecycleViewFragment(), "recyclerView")
                commit()
            }
            productListViewModel.setCurrentProductType(ProductSortType.POSTED_DATE_DESC)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterProductsBinding.bind(view)
        setUpToolbar()
        setObserve()
    }


    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = getFilterType.toString()
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        toolbar.menu.removeItem(R.id.notification)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort -> {
                    val bottomSheet = BottomSheetDialogSort(this)
                    bottomSheet.show(childFragmentManager, "bottomSheet")
                }
            }

            return@setOnMenuItemClickListener false
        }
    }


    private fun setObserve() {
        productListViewModel.data.observe(viewLifecycleOwner) {

            val fragment = childFragmentManager.findFragmentByTag("recyclerView")
            if (fragment is ProductRecycleViewFragment) {
                fragment.onSetData(it)
            }

        }

        productListViewModel.currentSortType.observe(viewLifecycleOwner) {
            val filterType = getFilterType
            if (!isSortTypeUpdate && filterType != null) {
                productListViewModel.getProductSummary(
                    userId,
                    it,
                    filterType
                )
                isSortTypeUpdate = true
            }
        }
    }


    override fun onItemClick(position: Int) {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showProductDetailFragment")
            replace(R.id.main_view_container, ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(
                        "currentProductId",
                        productListViewModel.data.value!![position].id
                    )
                    putBoolean("isCurrentUserProduct", false)
                }
            })
            commit()
        }
    }

    companion object {
        fun getInstant(type: ProductType): FilterProductFragment {
            return FilterProductFragment().apply {
                arguments = Bundle().apply {
                    putString("filterType", type.toString())
                }
            }
        }
    }

    override fun onSortTypeSelected(sortType: ProductSortType) {
        if (productListViewModel.currentSortType.value != sortType) {
            isSortTypeUpdate = false
            productListViewModel.setCurrentProductType(sortType)
        }
    }


}
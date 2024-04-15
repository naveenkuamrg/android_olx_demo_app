package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import com.application.R
import com.application.callbacks.ProductRecyclerFragmentCallback
import com.application.callbacks.ProductRecycleViewModelCallback
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentFilterProductsBinding
import com.application.helper.Utility
import com.application.model.ProductListItem
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.viewmodels.ProductListViewModel

class FilterProductFragment : Fragment(R.layout.fragment_filter_products), ProductRecyclerFragmentCallback,
    SortBottomSheetCallback {

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
                replace(
                    R.id.product_recycle_view,
                    ProductRecycleViewFragment.getInstance(false),
                    "recyclerView"
                )
                commit()
            }
            getFilterType?.let { productListViewModel.getProductSummary(it) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterProductsBinding.bind(view)
        binding.noData.errorText.text = "Sorry, No product availble for this category"
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
            productListViewModel.currentSortType.observe(viewLifecycleOwner) {sort->
                val fragment = childFragmentManager.findFragmentByTag("recyclerView")
                if (fragment is ProductRecycleViewModelCallback) {
                    fragment.reassignedAdapter()
                }
                when (sort) {
                    ProductSortType.POSTED_DATE_DESC -> {
                        productListViewModel.productListPostedDateDESC.observe(viewLifecycleOwner)
                        { pagingData -> setDataToAdapter(pagingData) }
                    }

                    ProductSortType.POSTED_DATE_ASC -> {
                        productListViewModel.productListPostedDateASC.observe(viewLifecycleOwner)
                        {pagingData -> setDataToAdapter(pagingData) }
                    }

                    ProductSortType.PRICE_ASC -> {
                        productListViewModel.productListPricesASC.observe(viewLifecycleOwner)
                        {pagingData -> setDataToAdapter(pagingData) }
                    }

                    ProductSortType.PRICE_DESC -> {
                        productListViewModel.productListPricesDESC.observe(viewLifecycleOwner)
                        {pagingData -> setDataToAdapter(pagingData) }
                    }
                }
            }
    }

    private fun setDataToAdapter(data: PagingData<ProductListItem>) {
        val fragment = childFragmentManager.findFragmentByTag("recyclerView")
        if (fragment is ProductRecycleViewModelCallback) {
            fragment.onSetData(data)
        }
    }


    override fun onProductSummaryClick(productId: Long) {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showProductDetailFragment")
            replace(R.id.main_view_container, ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(
                        "currentProductId",
                        productId
                    )
                    putBoolean("isCurrentUserProduct", false)
                }
            })
            commit()
        }
    }


    override fun onSortTypeSelected(sortType: ProductSortType) {
        if (productListViewModel.currentSortType.value != sortType) {
            isSortTypeUpdate = false
            productListViewModel.setCurrentProductType(sortType)
        }
    }

    override fun isListEmpty(isEmpty: Boolean) {
        Log.i("TAG empty",isEmpty.toString())
        if(isEmpty){
            binding.noData.noDataLayout.visibility = View.VISIBLE
        }else{
            binding.noData.noDataLayout.visibility = View.GONE
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


}
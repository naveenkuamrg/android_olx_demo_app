package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import com.application.R
import com.application.callbacks.SearchbarCallback
import com.application.callbacks.ProductRecyclerFragmentCallback
import com.application.callbacks.ProductRecycleViewModelCallback
import com.application.callbacks.ProductRecyclerFragmentWithFilterCallback
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentHomeBinding
import com.application.helper.Utility
import com.application.model.ProductListItem
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.viewmodels.ProductListViewModel


class HomeFragment : Fragment(R.layout.fragment_home),
    SortBottomSheetCallback, ProductRecyclerFragmentWithFilterCallback {

    lateinit var binding: FragmentHomeBinding

    lateinit var callback: SearchbarCallback

    private var isSortTypeUpdate = false

    private val productListViewModel: ProductListViewModel by viewModels { ProductListViewModel.FACTORY }

    var userId: Long = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = parentFragment as SearchbarCallback
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().apply {
                replace(
                    R.id.product_recycle_view,
                    ProductRecycleViewFragment.getInstance(true),
                    "recyclerView"
                )
                commit()
            }
            productListViewModel.setCurrentProductType(ProductSortType.POSTED_DATE_DESC)

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.noData.errorText.text = "Sorry,Stock Out"
        setUpSearchBar()
        setObserve()
    }

    override fun onFilterItemClick(productType: ProductType) {
        parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
            replace(R.id.main_view_container, FilterProductFragment.getInstant(productType))
            addToBackStack("Filter")
            commit()
        }
    }

    override fun onProductSummaryClick(productId: Long) {
        parentFragment?.parentFragmentManager?.popBackStackImmediate(
            "showProductDetailFragment",
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
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

    private fun setUpSearchBar() {
        val searchBar = binding.searchBar
        searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notification -> {
                    parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
                        addToBackStack("notification fragment")
                        replace(R.id.main_view_container, NotificationFragment())
                        commit()
                    }
                    return@setOnMenuItemClickListener true
                }

                R.id.sort -> {
                    if(childFragmentManager.findFragmentByTag("bottomSheet") != null){
                        return@setOnMenuItemClickListener true
                    }
                    val bottomSheet = BottomSheetDialogSort(this)
                    bottomSheet.show(childFragmentManager, "bottomSheet")
                    return@setOnMenuItemClickListener true
                }
            }

            return@setOnMenuItemClickListener false
        }
        callback.setUpWithSearchBar(searchBar)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userId = Utility.getLoginUserId(context)
    }

    private fun setObserve() {
        productListViewModel.currentSortType.observe(viewLifecycleOwner) { sort ->
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
                    { pagingData -> setDataToAdapter(pagingData) }
                }

                ProductSortType.PRICE_ASC -> {
                    productListViewModel.productListPricesASC.observe(viewLifecycleOwner)
                    { pagingData -> setDataToAdapter(pagingData) }
                }

                ProductSortType.PRICE_DESC -> {
                    productListViewModel.productListPricesDESC.observe(viewLifecycleOwner)
                    { pagingData -> setDataToAdapter(pagingData) }
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


    override fun onSortTypeSelected(sortType: ProductSortType) {
        if (productListViewModel.currentSortType.value != sortType) {
            isSortTypeUpdate = false
            productListViewModel.setCurrentProductType(sortType)
        }
    }

    override fun isListEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.noData.noDataLayout.visibility = View.VISIBLE
        } else {
            binding.noData.noDataLayout.visibility = View.GONE
        }
    }

}
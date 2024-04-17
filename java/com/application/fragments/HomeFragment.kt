package com.application.fragments

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import com.application.R
import com.application.callbacks.SearchbarCallback
import com.application.callbacks.ProductRecycleViewModelCallback
import com.application.callbacks.ProductRecyclerFragmentWithFilterCallback
import com.application.callbacks.ProductViewCallback
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentHomeBinding
import com.application.model.ProductListItem
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.viewmodels.NotificationViewModel
import com.application.viewmodels.ProductListViewModel


class HomeFragment : Fragment(R.layout.fragment_home),
    SortBottomSheetCallback, ProductRecyclerFragmentWithFilterCallback {

    lateinit var binding: FragmentHomeBinding

    private lateinit var searchbarCallback: SearchbarCallback

    private lateinit var productViewCallback: ProductViewCallback

    private var isSortTypeUpdate = false

    private val productListViewModel: ProductListViewModel by viewModels {
        ProductListViewModel.FACTORY
    }

    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModel.FACTORY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchbarCallback = parentFragment as SearchbarCallback
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
        notificationViewModel.getIsReadNotification()
        productViewCallback = parentFragment as ProductViewCallback
        setUpSearchBar()
        setObserve()
    }

    override fun onFilterItemClick(productType: ProductType) {
        parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
            replace(R.id.main_view_container, FilterProductListFragment.getInstant(productType))
            addToBackStack("Filter")
            commit()
        }
    }

    override fun onProductSummaryClick(productId: Long) {
        productViewCallback.onShowProductDetailsPage(productId,false)
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
        searchbarCallback.setUpWithSearchBar(searchBar)
    }


    private fun setObserve() {
        notificationViewModel.isUnReadNotification.observe(viewLifecycleOwner) {
            Log.i("TAG","isRead ${it}")
            val notificationIndicator =
                (binding.searchBar.menu.findItem(R.id.notification).icon as LayerDrawable).findDrawableByLayerId(
                    R.id.notification_indicator
                )
            if (it) {
                notificationIndicator.alpha = 225
            } else {
                notificationIndicator.alpha = 0
            }
        }
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
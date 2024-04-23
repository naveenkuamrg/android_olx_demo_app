package com.application.fragments

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.SearchbarCallback
import com.application.callbacks.ProductRecyclerFragmentWithFilterCallback
import com.application.callbacks.ProductViewCallback
import com.application.databinding.FragmentHomeBinding
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.viewmodels.NotificationViewModel


class BuyProductListFragment : SortableProductListFragment(R.layout.fragment_home),
    ProductRecyclerFragmentWithFilterCallback {

    lateinit var binding: FragmentHomeBinding

    private lateinit var searchbarCallback: SearchbarCallback

    private lateinit var productViewCallback: ProductViewCallback

    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModel.FACTORY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            productListViewModel.setCurrentProductType(ProductSortType.POSTED_DATE_DESC)
        }
        searchbarCallback = parentFragment as SearchbarCallback
        productViewCallback = parentFragment as ProductViewCallback
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.noData.errorText.text = "Sorry,Stock Out"
        notificationViewModel.getIsReadNotification()

        setUpSearchBar()
        setObserve()
    }

    override fun onFilterItemClick(productType: ProductType) {
        parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in_pop,
                R.anim.slide_out_pop
            )
            replace(R.id.main_view_container, FilterProductListFragment.getInstant(productType))
            addToBackStack("Filter")
            commit()
        }
    }

    override fun onProductSummaryClick(productId: Long) {
        productViewCallback.onShowProductDetailsPage(productId)
    }

    private fun setUpSearchBar() {
        val searchBar = binding.searchBar
        setUpToolbarMenuOnClickListener(searchBar)
        searchbarCallback.setUpWithSearchBar(searchBar)
    }

    private fun setUpToolbarMenuOnClickListener(toolbar: Toolbar) {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort -> {
                    val bottomSheet = BottomSheetDialogSort()
                    bottomSheet.show(childFragmentManager, "bottomSheet")
                    return@setOnMenuItemClickListener true
                }

                R.id.notification -> {
                    parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
                        addToBackStack("notification fragment")
                        replace(R.id.main_view_container, NotificationFragment())
                        commit()
                    }
                    return@setOnMenuItemClickListener false
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun setObserve() {
        super.setObserve()
        notificationViewModel.isUnReadNotification.observe(viewLifecycleOwner) {
            val notificationIndicator =
                (binding.searchBar.menu.findItem(R.id.notification).icon as
                        LayerDrawable).findDrawableByLayerId(
                    R.id.notification_indicator
                )
            if (it) {
                notificationIndicator.alpha = 225
            } else {
                notificationIndicator.alpha = 0
            }
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
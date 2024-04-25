package com.application.fragments

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import com.application.helper.Utility.commitWithSlideAnimation
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.callbacks.SearchbarCallback
import com.application.callbacks.ProductRecyclerFragmentWithFilterCallback
import com.application.callbacks.ProductViewCallback
import com.application.databinding.FragmentBuyZoneBinding
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.viewmodels.NotificationViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator


class BuyProductListFragment : SortableProductListFragment(R.layout.fragment_buy_zone),
    ProductRecyclerFragmentWithFilterCallback {

    lateinit var binding: FragmentBuyZoneBinding

    override lateinit var progressIndicator: CircularProgressIndicator

    override lateinit var recyclerView: RecyclerView


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
        binding = FragmentBuyZoneBinding.bind(view)
        recyclerView = binding.recycleView
        progressIndicator = binding.progressCircular
        super.onViewCreated(view, savedInstanceState)
        binding.noData.errorText.text = "Sorry,Stock Out"
        notificationViewModel.getIsReadNotification()
        setUpSearchBar()
        setObserve()
    }

    override fun onFilterItemClick(productType: ProductType) {
        parentFragment?.parentFragmentManager?.commitWithSlideAnimation(
            "Filter",
            FilterProductListFragment.getInstant(productType),
            R.id.main_view_container
        )
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
                    if (childFragmentManager.findFragmentByTag("bottomSheet") != null) {
                        return@setOnMenuItemClickListener true
                    }
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
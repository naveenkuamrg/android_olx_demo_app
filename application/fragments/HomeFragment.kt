package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.SearchbarCallback
import com.application.callbacks.OnFilterItemClickListener
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentHomeBinding
import com.application.helper.Utility
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.viewmodels.ProductListViewModel
//import com.application.viewmodels.ProductRecycleViewModel
import com.application.viewmodels.SearchProductViewModel

class HomeFragment : Fragment(R.layout.fragment_home),
    SortBottomSheetCallback,OnFilterItemClickListener {

    lateinit var binding: FragmentHomeBinding

    lateinit var callback: SearchbarCallback

    private var isSortTypeUpdate = false

    private val productListViewModel: ProductListViewModel by activityViewModels { ProductListViewModel.FACTORY }

    var userId: Long = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = parentFragment as SearchbarCallback
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
        binding = FragmentHomeBinding.bind(view)
        setUpSearchBar()
        setObserve()
    }

    override fun onFilterItemClick(productType: ProductType) {
        parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
            replace(R.id.main_view_container,FilterProductFragment.getInstant(productType))
            addToBackStack("Filter")
            commit()
        }
    }

    override fun onItemClick(position: Int) {
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
                        productListViewModel.data.value!![position - 1].id
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
                }

                R.id.sort -> {
                    val bottomSheet = BottomSheetDialogSort(this)
                    bottomSheet.show(childFragmentManager, "bottomSheet")
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
        productListViewModel.data.observe(viewLifecycleOwner) {

            val fragment = childFragmentManager.findFragmentByTag("recyclerView")
            if (fragment is ProductRecycleViewFragment) {
                fragment.onSetData(it)
            }

        }


        productListViewModel.currentSortType.observe(viewLifecycleOwner) {
            if (!isSortTypeUpdate) {
                productListViewModel.getProductSummary(
                    userId,
                    it
                )
                isSortTypeUpdate = true
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
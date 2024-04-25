package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.databinding.FragmentProductListBinding
import com.application.helper.Utility.commitWithSlideAnimation
import com.google.android.material.progressindicator.CircularProgressIndicator

class ProductListFragment : BaseProductListFragment(R.layout.fragment_product_list) {

    lateinit var binding: FragmentProductListBinding
    override lateinit var recyclerView: RecyclerView
    override lateinit var progressIndicator: CircularProgressIndicator

    private val getActionType: Int?
        get() {
            return arguments?.getInt(TYPE, -1)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProductListBinding.bind(view)
        recyclerView = binding.recycleView
        progressIndicator = binding.progressCircular
        super.onViewCreated(view, savedInstanceState)

        when (getActionType) {
            FAVOURITE_LIST ->
                binding.noData.errorText.text = "Your favorite list is empty"


            INTERESTED_LIST ->
                binding.noData.errorText.text = "Your's interested list is empty"

        }
        setToolbar()
        setObserve()
    }

    private fun setObserve() {

        when (getActionType) {
            FAVOURITE_LIST ->
                productListViewModel.favoriteProductList.observe(viewLifecycleOwner) {
                    setData(it)
                }


            INTERESTED_LIST ->
                productListViewModel.interestedProductList.observe(viewLifecycleOwner) {
                    setData(it)

                }
        }


    }


    private fun setToolbar() {
        val toolbar = binding.toolbar


        when (getActionType) {
            FAVOURITE_LIST -> toolbar.title = "Favourite"
            INTERESTED_LIST -> toolbar.title = "Interested"
        }
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
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
            },R.id.main_view_container)
    }

    override fun isListEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.noData.noDataLayout.visibility = View.VISIBLE
        } else {
            binding.noData.noDataLayout.visibility = View.GONE
        }
    }

    companion object {
        val TYPE = "ACTION_TYPE"
        val FAVOURITE_LIST: Int = 0
        val INTERESTED_LIST: Int = 1
        fun getInstances(list: Int): ProductListFragment {
            return ProductListFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE, list)
                }
            }
        }
    }

}
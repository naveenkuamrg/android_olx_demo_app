package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.ProductRecyclerFragmentCallback
import com.application.databinding.FragmentProductListBinding
import com.application.viewmodels.ProductListViewModel

class InterestedProductListFragment: Fragment(R.layout.fragment_product_list),ProductRecyclerFragmentCallback {

    lateinit var binding: FragmentProductListBinding

    private val productListViewModel: ProductListViewModel by viewModels {ProductListViewModel.FACTORY}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            childFragmentManager.beginTransaction().apply {
                replace(R.id.recycler_view_container,ProductRecycleViewFragment(),"recyclerView")
                commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductListBinding.bind(view)
        binding.noData.errorText.text = "Your's interested product is empty"
        setToolbar()
        setObserve()
    }

    private fun setObserve(){
        productListViewModel.interestedProductList.observe(viewLifecycleOwner){

                val fragment = childFragmentManager.findFragmentByTag("recyclerView")
                if (fragment is ProductRecycleViewFragment) {
                    fragment.onSetData(it)
            }
        }
    }

    private fun setToolbar(){
        val toolbar = binding.toolbar
        toolbar.title = "Interested"
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
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

    override fun isListEmpty(isEmpty: Boolean) {
        if(isEmpty){
            binding.noData.noDataLayout.visibility = View.VISIBLE
        }else{
            binding.noData.noDataLayout.visibility = View.GONE
        }
    }

}
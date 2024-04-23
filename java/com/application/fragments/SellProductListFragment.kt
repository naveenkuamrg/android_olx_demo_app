package com.application.fragments

import android.os.Bundle
import android.view.View
import com.application.R
import com.application.callbacks.ProductViewCallback
import com.application.databinding.FragmentSellZoneBinding

class SellProductListFragment : BaseProductListFragment(R.layout.fragment_sell_zone){
    lateinit var binding: FragmentSellZoneBinding


    private lateinit var callBack: ProductViewCallback




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellZoneBinding.bind(view)
        callBack = parentFragment as ProductViewCallback
        binding.noData.errorText.text = "You din't Post any Product"
        setOnClickListenerAddProduct()
        setObserve()

    }

    private fun setOnClickListenerAddProduct() {
        binding.addProduct.setOnClickListener {
            callBack.onShowProductEditDetailPage()
        }
    }

    override fun onProductSummaryClick(productId: Long) {
        callBack.onShowProductDetailsPage(productId)
    }

    override fun isListEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.noData.noDataLayout.visibility = View.VISIBLE
        } else {
            binding.noData.noDataLayout.visibility = View.GONE
        }
    }

    private fun setObserve() {
        productListViewModel.sellProductList.observe(viewLifecycleOwner) {
            setData(it)
//            val fragment = childFragmentManager.findFragmentByTag("recyclerView")
//            if (fragment is ProductRecycleViewFragment) {
//                fragment.onSetData(it)
//            }
        }
    }
}
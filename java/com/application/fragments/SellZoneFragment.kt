package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.ProductRecyclerFragmentCallback
import com.application.callbacks.ProductViewCallback
import com.application.databinding.FragmentSellZoneBinding
import com.application.viewmodels.ProductListViewModel

class SellZoneFragment : Fragment(R.layout.fragment_sell_zone), ProductRecyclerFragmentCallback {
    lateinit var binding: FragmentSellZoneBinding


    private val productListViewModel: ProductListViewModel by viewModels { ProductListViewModel.FACTORY }


    private lateinit var callBack: ProductViewCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().apply {
                add(
                    R.id.fragment_container_view_tag,
                    ProductRecycleViewFragment.getInstance(false),
                    "recyclerView"
                )
                commit()
            }
        }
    }


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
        callBack.onShowProductDetailsPage(productId,true)
    }

    override fun isListEmpty(isEmpty: Boolean) {
        if(isEmpty){
            binding.noData.noDataLayout.visibility = View.VISIBLE
        }else{
            binding.noData.noDataLayout.visibility = View.GONE
        }
    }

    private fun setObserve() {
        productListViewModel.sellProductList.observe(viewLifecycleOwner) {
            val fragment = childFragmentManager.findFragmentByTag("recyclerView")
            if (fragment is ProductRecycleViewFragment) {
                fragment.onSetData(it)
            }
        }
    }
}
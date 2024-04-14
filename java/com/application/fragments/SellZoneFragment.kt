package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.OnItemClickListener
import com.application.callbacks.ProductViewCallback
import com.application.databinding.FragmentSellZoneBinding
import com.application.viewmodels.ProductListViewModel

class SellZoneFragment : Fragment(R.layout.fragment_sell_zone), OnItemClickListener {
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
        setOnClickListenerAddProduct()
        setObserve()

    }

    private fun setOnClickListenerAddProduct() {
        binding.addProduct.setOnClickListener {
            callBack.onShowProductEditDetailPage()
        }
    }

    override fun onItemClick(position: Int) {
        callBack.onShowProductDetailsPage(position.toLong())
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
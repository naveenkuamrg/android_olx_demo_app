package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

    var userId: Long = -1L

    private lateinit var callBack: ProductViewCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().apply {
                add(
                    R.id.fragment_container_view_tag,
                    ProductRecycleViewFragment(),
                    "recyclerView"
                )
                commit()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userId = context.getSharedPreferences("mySharePref", AppCompatActivity.MODE_PRIVATE)
            .getString("userId", "-1L")!!.toLong()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellZoneBinding.bind(view)
        callBack = parentFragment as ProductViewCallback
        setOnClickListenerAddProduct()
        setObserve()
        productListViewModel.getProductSummary(
            userId
        )
    }

    private fun setOnClickListenerAddProduct() {
        binding.addProduct.setOnClickListener {
            callBack.onShowProductEditDetailPage()
        }
    }

    override fun onItemClick(position: Int) {
        callBack.onShowProductDetailsPage(productListViewModel.data.value!![position].id)
    }

    private fun setObserve() {
        productListViewModel.data.observe(viewLifecycleOwner) {
            val fragment = childFragmentManager.findFragmentByTag("recyclerView")
            if (fragment is ProductRecycleViewFragment) {
                fragment.onSetData(it)
            }
        }
    }
}
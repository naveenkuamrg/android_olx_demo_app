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
import com.application.viewmodels.SellZoneViewModel

class SellZoneFragment : Fragment(R.layout.fragment_sell_zone), OnItemClickListener {
    lateinit var binding: FragmentSellZoneBinding

    val viewModel: SellZoneViewModel by viewModels { SellZoneViewModel.FACTORY }

    var userId: Long = -1L

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

    lateinit var callBack: ProductViewCallback
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellZoneBinding.bind(view)
        callBack = parentFragment as ProductViewCallback
        setOnClickListenerAddProduct()
        setObserve()
        viewModel.getProductSummary(
            userId
        )
    }

    private fun setOnClickListenerAddProduct() {
        binding.addProduct.setOnClickListener {
            callBack.onShowProductEditDetailPage()
        }
    }

    override fun onItemClick(position: Int) {
        callBack.onShowProductDetailsPage(viewModel.data.value!![position].id)
    }

    private fun setObserve() {
        viewModel.data.observe(viewLifecycleOwner) {
            val fragment = childFragmentManager.findFragmentByTag("recyclerView")
            if (fragment is ProductRecycleViewFragment) {
                fragment.onSetData(it)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) {

            }
            if (it) {

            }
        }
    }
}
package com.application.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.application.R
import com.application.callbacks.ProductSummaryAdapterCallBack
import com.application.callbacks.RecycleProductViewCallback

class HomeFragment : Fragment(R.layout.fragment_home), RecycleProductViewCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.product_recycle_view, ProductRecycleViewFragment())
                commit()
            }
        }
    }

    override fun productItemIsSelected(productId: Long) {
        parentFragment?.parentFragmentManager?.popBackStackImmediate(
            "showProductDetailFragment",
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
            addToBackStack("showProductDetailFragment")
            replace(R.id.main_view_container, ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong("currentProductId",productId)
                    putString("fragment","home")
                }
            })
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG HomeFragment","onDestroy")
    }

}
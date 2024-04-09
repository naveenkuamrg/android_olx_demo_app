package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.OnItemClickListener
import com.application.databinding.FragmentProductListBinding
import com.application.helper.Utility
import com.application.viewmodels.YoursActivityPageViewModel

class InterestedProductListFragment: Fragment(R.layout.fragment_product_list),OnItemClickListener {

    lateinit var binding: FragmentProductListBinding

    val viewmodel: YoursActivityPageViewModel by viewModels {YoursActivityPageViewModel.FACTORY}

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
        setToolbar()
        setObserve()
        viewmodel.getInterestedProductSummary(Utility.getLoginUserId(requireContext()))
    }

    private fun setObserve(){
        viewmodel.data.observe(viewLifecycleOwner){
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

    override fun onItemClick(position: Int) {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showProductDetailFragment")
            replace(R.id.main_view_container, ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(
                        "currentProductId",
                        viewmodel.data.value!![position].id
                    )
                    putBoolean("isCurrentUserProduct", false)
                }
            })
            commit()
        }
    }

}
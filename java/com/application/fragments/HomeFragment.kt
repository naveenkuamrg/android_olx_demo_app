package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.SearchAdapter
import com.application.callbacks.HomeFragmentCallback
import com.application.callbacks.RecycleProductViewCallback
import com.application.databinding.FragmentHomeBinding
import com.application.viewmodels.SearchProductViewModel
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home), RecycleProductViewCallback{

    lateinit var binding: FragmentHomeBinding

    lateinit var callback: HomeFragmentCallback


    val viewModel: SearchProductViewModel by viewModels { SearchProductViewModel.FACTORY }

    val userId: Long = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.product_recycle_view, ProductRecycleViewFragment())
                commit()
            }
        }
        callback = parentFragment as HomeFragmentCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        callback.getSearchView().setupWithSearchBar(binding.searchBar)
        setUpSearchBar()
        setObserve()
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
                    putLong("currentProductId", productId)
                    putString("fragment", "home")
                }
            })
            commit()
        }
    }

    private fun setUpSearchBar() {
        val searchRecyclerView = callback.getSearchRecyclerView()
        searchRecyclerView.adapter = SearchAdapter(this)
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        callback.getSearchView().editText.addTextChangedListener {
            val searchTerm = it.toString().trim().lowercase(Locale.ROOT)

            viewModel.search(searchTerm.trim(), 1)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().getSharedPreferences("mySharePref", AppCompatActivity.MODE_PRIVATE)
            .getString("userId","-1L")!!.toLong()
    }

    private fun setObserve() {
        val adapter = SearchAdapter(this)
        callback.getSearchRecyclerView().adapter = adapter
        viewModel.searchResult.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }
    }




}
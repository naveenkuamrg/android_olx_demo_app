package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.adapter.SearchAdapter
import com.application.callbacks.HomeFragmentCallback
import com.application.callbacks.ItemOnClickCallback
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentHomeBinding
import com.application.model.ProductSortType
import com.application.viewmodels.HomeViewModel
import com.application.viewmodels.ProductRecycleViewModel
import com.application.viewmodels.SearchProductViewModel
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home), ItemOnClickCallback,
    SortBottomSheetCallback {

    lateinit var binding: FragmentHomeBinding

    lateinit var callback: HomeFragmentCallback


    private val searchProductViewModel: SearchProductViewModel by viewModels { SearchProductViewModel.FACTORY }

    private val productRecycleViewModel: ProductRecycleViewModel by viewModels {
        ProductRecycleViewModel.FACTORY
    }

    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.FACTORY }



    var userId: Long = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = parentFragment as HomeFragmentCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setUpSearchBar()
        setObserve()
        setUpRecycleView()
        productRecycleViewModel.getBuyProductSummary(
            userId,
            homeViewModel.currentSortType
        )
    }

    override fun itemOnClick(productId: Long) {
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
        callback.getSearchView().setupWithSearchBar(binding.searchBar)
        binding.searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notification -> {}
                R.id.sort -> {
                    val bottomSheet = BottomSheetDialogSort(this)
                    bottomSheet.show(childFragmentManager, "bottomSheet")
                }
            }

            return@setOnMenuItemClickListener true
        }
        val searchRecyclerView = callback.getSearchRecyclerView()
        searchRecyclerView.adapter = SearchAdapter(this)
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        callback.getSearchView().editText.addTextChangedListener {
            val searchTerm = it.toString().trim().lowercase(Locale.ROOT)

            searchProductViewModel.search(searchTerm.trim(), userId)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userId = context.getSharedPreferences("mySharePref", AppCompatActivity.MODE_PRIVATE)
            .getString("userId", "-1L")!!.toLong()
    }

    private fun setObserve() {
        val adapter = SearchAdapter(this)
        callback.getSearchRecyclerView().adapter = adapter
        searchProductViewModel.searchResult.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }

        productRecycleViewModel.data.observe(viewLifecycleOwner) {
            (binding.productSummaryRecyclerView.adapter as ProductSummaryAdapter).saveData(it)
        }
        productRecycleViewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                binding.progressCircular.visibility = View.GONE
                binding.productSummaryRecyclerView.visibility = View.VISIBLE
            }
            if(it){
                binding.progressCircular.visibility = View.VISIBLE
                binding.productSummaryRecyclerView.visibility = View.GONE
            }
        }

    }



    override fun onClick(sortType: ProductSortType) {
        productRecycleViewModel.getBuyProductSummary(
            userId,
           sortType
        )
    }

    private fun setUpRecycleView() {
        val recyclerView = binding.productSummaryRecyclerView
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        requireContext().getDrawable(
            R.drawable.recycleview_divider
        )?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProductSummaryAdapter(this)

    }





}
package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.adapter.SearchAdapter
import com.application.callbacks.ItemOnClickCallback
import com.application.callbacks.ProductViewCallback
import com.application.databinding.FragmentSellZoneBinding
import com.application.model.ProductSortType
import com.application.viewmodels.ProductRecycleViewModel
import com.application.viewmodels.SellZoneViewModel

class SellZoneFragment : Fragment(R.layout.fragment_sell_zone), ItemOnClickCallback {
    lateinit var binding: FragmentSellZoneBinding

    val viewModel: SellZoneViewModel by viewModels { SellZoneViewModel.FACTORY }

    private val productRecycleViewModel: ProductRecycleViewModel by viewModels {
        ProductRecycleViewModel.FACTORY
    }

    var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
//            childFragmentManager.beginTransaction().apply {
//                add(R.id.fragment_container_view_tag, ProductRecycleViewFragment())
//                commit()
//            }
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
        setUpRecycleView()
        setObserve()
        productRecycleViewModel.getPostProductSummary(
            userId
        )
    }

    private fun setOnClickListenerAddProduct() {
        binding.addProduct.setOnClickListener {
            callBack.showProductEditDetailPage()
        }
    }

    override fun itemOnClick(productId: Long) {
        callBack.showProductDetailsPage(productId)
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

    private fun setObserve() {
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
}
package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.SearchAdapter
import com.application.callbacks.SearchbarCallback
import com.application.callbacks.ProductViewCallback
import com.application.callbacks.ProfileFragmentCallback
import com.application.databinding.FragmentMainBinding
import com.application.helper.Utility
import com.application.helper.Utility.commitWithSlideAnimation
import com.application.viewmodels.SearchProductViewModel
import com.google.android.material.search.SearchBar
import java.util.Locale

class MainFragment : Fragment(R.layout.fragment_main), ProfileFragmentCallback,
    ProductViewCallback, SearchbarCallback {
    lateinit var binding: FragmentMainBinding

    private val searchProductViewModel: SearchProductViewModel by viewModels {
        SearchProductViewModel.FACTORY
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            addHomeFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setOnItemSelectListener()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i("TAG onBackPressCallback", "1")
                val bottomNavigation = binding.navigationBar
                if (childFragmentManager.backStackEntryCount > 1) {
                    childFragmentManager.popBackStackImmediate()
                    when (childFragmentManager.fragments[0]) {
                        is ProfileFragment -> {
                            bottomNavigation.selectedItemId = R.id.profile
                        }

                        is SellProductListFragment -> {
                            bottomNavigation.selectedItemId = R.id.sell_zone
                        }

                        is BuyProductListFragment -> {
                            bottomNavigation.selectedItemId = R.id.home
                        }
                    }
                } else {
                    if (binding.searchView.isShowing) {
                        binding.searchView.hide()
                    } else {
                        requireActivity().finish()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
        setObserve()
    }

    private fun setOnItemSelectListener() {
        binding.navigationBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    if (childFragmentManager.fragments[0] is BuyProductListFragment) {
                        return@setOnItemSelectedListener true
                    }
                    childFragmentManager.popBackStack(
                        "home",
                        0
                    )
                }

                R.id.sell_zone -> {
                    if (childFragmentManager.fragments[0] is SellProductListFragment) {
                        return@setOnItemSelectedListener true
                    }

                    childFragmentManager.popBackStack(
                        "sellZone",
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    childFragmentManager.beginTransaction().apply {
                        replace(
                            R.id.bottom_navigation_fragment_view_container,
                            SellProductListFragment()
                        )
                        addToBackStack("sellZone")
                        commit()
                    }
                }

                R.id.profile -> {
                    if (childFragmentManager.fragments[0] is ProfileFragment) {
                        return@setOnItemSelectedListener true
                    }
                    childFragmentManager.popBackStackImmediate(
                        "profile",
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    val transaction = childFragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.bottom_navigation_fragment_view_container,
                        ProfileFragment()
                    )
                    transaction.addToBackStack("profile")
                    transaction.commit()
                }
            }


            return@setOnItemSelectedListener true
        }
    }

    private fun addHomeFragment() {
        childFragmentManager.commitWithSlideAnimation(
            "home",
            BuyProductListFragment(),
            R.id.bottom_navigation_fragment_view_container,
        )
    }

    override fun onShowEditPage() {
        parentFragmentManager.commitWithSlideAnimation(
            "editProfileFragment",
            EditProfileFragment(),
            R.id.main_view_container,
        )
    }

    override fun onShowLoginPage() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.main_view_container, LoginFragment())
            commit()
        }
    }

    override fun onShowChangePasswordPage() {
        parentFragmentManager.commitWithSlideAnimation(
            "changePasswordFragment", ChangePasswordFragment(), R.id.main_view_container
        )
    }

    override fun onShowActivityPage() {
        parentFragmentManager.commitWithSlideAnimation(
            "showActivityFragment",
            ActivityPageFragment(),
            R.id.main_view_container
        )

    }

    override fun onShowProductEditDetailPage() {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showEditProductFragment")
            replace(R.id.main_view_container, EditProductFragment.getInstant(-1))
            commit()
        }
    }

    override fun onShowProductDetailsPage(productId: Long) {
        parentFragmentManager.commitWithSlideAnimation(
            "showProductDetailFragment",
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong("currentProductId", productId)
                }
            }, R.id.main_view_container
        )

    }


    private fun setObserve() {
        searchProductViewModel.searchResult.observe(viewLifecycleOwner) {
            val adapter = binding.searchResult.adapter
            if (adapter is SearchAdapter) {
                adapter.submitData(it)
            }
        }
    }

    override fun setUpWithSearchBar(searchBar: SearchBar) {
        val searchView = binding.searchView
        searchView.setupWithSearchBar(searchBar)
        searchView.editText.addTextChangedListener {
            val searchTerm = it.toString().trim().lowercase(Locale.ROOT)
            searchProductViewModel.search(searchTerm, Utility.getLoginUserId(requireContext()))
        }
        val searchRecyclerView = binding.searchResult
        searchRecyclerView.adapter = SearchAdapter {
            parentFragmentManager.commitWithSlideAnimation(
                "showProductDetailFragment",
                ProductDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putLong("currentProductId", it)
                    }
                }, R.id.main_view_container
            )
        }
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}
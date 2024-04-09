package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.callbacks.HomeFragmentCallback
import com.application.callbacks.ProductViewCallback
import com.application.callbacks.ProfileFragmentCallback
import com.application.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main), ProfileFragmentCallback,
    ProductViewCallback, HomeFragmentCallback {
    lateinit var binding: FragmentMainBinding
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
                val bottomNavigation = binding.navigationBar
                if (childFragmentManager.backStackEntryCount > 1) {
                    childFragmentManager.popBackStackImmediate()
                    when (childFragmentManager.fragments[0]) {
                        is ProfileFragment -> {
                            bottomNavigation.selectedItemId = R.id.profile
                        }

                        is SellZoneFragment -> {
                            bottomNavigation.selectedItemId = R.id.sell_zone
                        }

                        is HomeFragment -> {
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
    }

    private fun setOnItemSelectListener() {
        binding.navigationBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    if (childFragmentManager.fragments[0] is HomeFragment) {
                        return@setOnItemSelectedListener true
                    }
                    childFragmentManager.popBackStackImmediate(
                        "home",
                       0
                    )
                }

                R.id.sell_zone -> {
                    if (childFragmentManager.fragments[0] is SellZoneFragment) {
                        return@setOnItemSelectedListener true
                    }

                    childFragmentManager.popBackStack(
                        "sellZone",
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    childFragmentManager.beginTransaction().apply {
                        replace(R.id.bottom_navigation_fragment_view_container, SellZoneFragment())

                        addToBackStack("sellZone")
                        commit()
                    }
                }

                R.id.profile -> {
                    if (childFragmentManager.fragments[0] is ProfileFragment) {
                        return@setOnItemSelectedListener true
                    }
                    childFragmentManager.popBackStack(
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
    fun addHomeFragment() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.bottom_navigation_fragment_view_container, HomeFragment())
        transaction.addToBackStack("home")
        transaction.commit()
    }
    override fun onShowEditPage() {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("editProfileFragment")
            replace(R.id.main_view_container, EditProfileFragment())
            commit()
        }
    }
    override fun onShowLoginPage() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.main_view_container, LoginFragment())
            commit()
        }
    }
    override fun onShowChangePasswordPage() {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("changePasswordFragment")
            replace(R.id.main_view_container, ChangePasswordFragment())
            commit()
        }
    }

    override fun onShowActivityPage() {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showActivityFragment")
            replace(R.id.main_view_container,YoursActivityFragment())
            commit()
        }
    }

    override fun onShowProductEditDetailPage() {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showEditProductFragment")
            replace(R.id.main_view_container, EditProductFragment.getInstant(-1))
            commit()
        }
    }
    override fun onShowProductDetailsPage(productId: Long) {

        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showProductDetailFragment")
            replace(R.id.main_view_container, ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong("currentProductId", productId)
                }
            })
            commit()
        }
    }

    override fun getSearchView(): com.google.android.material.search.SearchView {
        return binding.searchView
    }

    override fun getSearchRecyclerView(): RecyclerView {
       return binding.searchResult
    }
}
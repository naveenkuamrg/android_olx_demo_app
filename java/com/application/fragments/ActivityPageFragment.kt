package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.application.helper.Utility.commitWithSlideAnimation
import com.application.R
import com.application.databinding.FragmentActivityBinding

class ActivityPageFragment : Fragment(R.layout.fragment_activity) {
    lateinit var binding: FragmentActivityBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActivityBinding.bind(view)
        setUpToolbar()
        setupView()
    }


    private fun setupView() {
        val favouriteListNavigator = binding.favourite
        favouriteListNavigator.imageFilterView.setImageResource(R.drawable.ic_favorite_outline)
        favouriteListNavigator.textLabel.text = "Favourite"
        favouriteListNavigator.navigator.setOnClickListener {
            replaceFragment(
                "FavoriteFragment",
                ProductListFragment.getInstances(ProductListFragment.FAVOURITE_LIST)
            )
        }


        val interestedListNavigator = binding.interested
        interestedListNavigator.imageFilterView.setImageResource(R.drawable.ic_interested)
        interestedListNavigator.textLabel.text = "Interested"
        interestedListNavigator.navigator.setOnClickListener {
            replaceFragment(
                "InterestedFragment",
                ProductListFragment.getInstances(ProductListFragment.INTERESTED_LIST)
            )
        }
    }

    private fun replaceFragment(addToBackStack: String, fragment: Fragment) {
        parentFragmentManager.commitWithSlideAnimation(
            addToBackStack,
            fragment,
            R.id.main_view_container
        )
    }


    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
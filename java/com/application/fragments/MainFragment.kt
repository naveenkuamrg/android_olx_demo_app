package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.application.R
import com.application.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    lateinit var binding: FragmentMainBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setOnItemSelectListener()
        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(childFragmentManager.backStackEntryCount > 0) {
                    childFragmentManager.popBackStack()
                }else{
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,onBackPressedCallback)

    }

    private fun setOnItemSelectListener(){
        binding.navigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    childFragmentManager.popBackStack("home",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.bottom_navigation_fragment_view_container,HomeFragment())
                    transaction.addToBackStack("home")
                    transaction.commit()
                }
                R.id.profile->{
                    childFragmentManager.popBackStack("profile",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    val transaction = childFragmentManager.beginTransaction()
                    transaction.replace(R.id.bottom_navigation_fragment_view_container,ProfileFragment())
                    transaction.addToBackStack("profile")
                    transaction.commit()
                }
            }


            return@setOnItemSelectedListener true
        }
    }



}
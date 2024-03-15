package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.application.R
import com.application.callbacks.ProfileFragmentCallBack
import com.application.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main),ProfileFragmentCallBack {
    lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            addHomeFragment()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setOnItemSelectListener()
        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if(childFragmentManager.backStackEntryCount > 1) {
                    childFragmentManager.popBackStack()
                    Log.i("TAG",childFragmentManager.fragments[0].toString())
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
                    addHomeFragment()
                }
                R.id.sell_zone->{

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

    fun addHomeFragment(){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.bottom_navigation_fragment_view_container,HomeFragment())
        transaction.addToBackStack("home")
        transaction.commit()
    }

    override fun showEditFragment() {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("editProfileFragment")
            replace(R.id.main_view_container,EditProfileFragment())
            commit()
        }
    }

    override fun showLoginFragment() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.main_view_container,LoginFragment())
            commit()
        }
    }

    override fun showChangePasswordFragment() {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("changePasswordFragment")
            replace(R.id.main_view_container,ChangePasswordFragment())
            commit()
        }
    }

}
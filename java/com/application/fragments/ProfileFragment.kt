package com.application.fragments

import android.os.Binder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.application.R
import com.application.databinding.FragmentProfileBinding
import com.application.viewmodels.ProfilePageViewModel

class ProfileFragment() : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding

    val viewModel : ProfilePageViewModel by activityViewModels {ProfilePageViewModel.FACTORY}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        setMenuItemClickListenerForMenu()
        addObserver()
        val sharedPreferences = requireActivity().getSharedPreferences("mySharePref", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences.getString("userId","-1")?.let { viewModel.showProfile(it.toLong()) }
    }


    private fun setMenuItemClickListenerForMenu(){
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.logout -> {
                    requireActivity().getSharedPreferences("mySharePref",
                        AppCompatActivity.MODE_PRIVATE
                    ).edit().apply {
                        remove("userId")
                        apply()
                    }
                    parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_view_container,LoginFragment())
                        commit()
                    }
                }
                R.id.edit->{
                    parentFragment?.parentFragmentManager?.beginTransaction()?.apply {
                        addToBackStack("editProfileFragment")
                        replace(R.id.main_view_container,EditProfileFragment())
                        commit()
                    }
                }
            }
            return@setOnMenuItemClickListener true
        }
    }


    private fun addObserver(){
        viewModel.profile.observe(viewLifecycleOwner){profile ->
            binding.userName.text = profile.name
            binding.emailTextview.text = profile.email
            binding.phoneNumberTextview.text = profile.phoneNumber
        }
    }
}
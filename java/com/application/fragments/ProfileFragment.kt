package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.application.R
import com.application.callbacks.ProfileFragmentCallBack
import com.application.databinding.FragmentProfileBinding
import com.application.viewmodels.ProfilePageViewModel

class ProfileFragment() : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding

    val viewModel: ProfilePageViewModel by activityViewModels { ProfilePageViewModel.FACTORY }

    private lateinit var callBack: ProfileFragmentCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callBack = parentFragment as ProfileFragmentCallBack
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        addObserver()

        val sharedPreferences = requireActivity().getSharedPreferences(
            "mySharePref", AppCompatActivity.MODE_PRIVATE
        )
        sharedPreferences.getString("userId", "-1")?.let {
            viewModel.showProfile(it.toLong())
        }
        setNavigatorView()

    }

    private fun setNavigatorView() {
        binding.edit.apply {
            textLabel.text = "Edit Profile"
            imageFilterView.setImageResource(R.drawable.ic_edit)
            navigator.setOnClickListener {
                callBack.showEditFragment()
            }
        }

        binding.changePassword.apply {
            textLabel.text = "Change password"
            imageFilterView.setImageResource(R.drawable.ic_lock)
            navigator.setOnClickListener {
                callBack.showChangePasswordFragment()
            }
        }
        binding.logout.apply {
            textLabel.text = "Logout"
            imageFilterView.setImageResource(R.drawable.ic_power_off)
            shapeableImageView.visibility = View.GONE
            navigator.setOnClickListener {
                AlertDialog.Builder(requireContext()).apply {
                    setMessage("Would you like to sign out")
                    setPositiveButton("Yes"){ _, _ ->
                        requireActivity().getSharedPreferences(
                            "mySharePref",
                            AppCompatActivity.MODE_PRIVATE
                        ).edit().apply {
                            remove("userId")
                            apply()
                        }
                        callBack.showLoginFragment()
                    }
                   setNegativeButton("No"){_,_->}
                    show()
                }

            }
        }

    }


    private fun addObserver() {
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.userName.text = profile.name
            binding.emailTextview.text = profile.email
            binding.phoneNumberTextview.text = profile.phoneNumber
            if(profile.profileImage != null){
                binding.userDp.setImageBitmap(profile.profileImage)
            }else{
                binding.userDp.setImageResource(R.drawable.ic_profile_outline)
            }
        }
    }
}
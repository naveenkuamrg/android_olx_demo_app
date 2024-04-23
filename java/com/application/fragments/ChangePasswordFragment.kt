package com.application.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.databinding.FragmentChangePasswordBinding
import com.application.helper.Validator
import com.application.viewmodels.ChangePasswordViewModel

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding

    private val viewModel: ChangePasswordViewModel by viewModels { ChangePasswordViewModel.FACTORY }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangePasswordBinding.bind(view)
        setUpToolbar()
        setOnClickListenerForButton()
        setObserve()


        binding.reNewPasswordEditText.addTextChangedListener {
            if (it.toString() != binding.newPasswordEditText.text.toString()) {
                binding.reNewPasswordEditTextlayout.error = "not match"
            } else {
                binding.reNewPasswordEditTextlayout.error = null
            }
        }
    }

    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setOnClickListenerForButton() {
        binding.changePasswordButton.setOnClickListener {
            val newPassword = binding.newPasswordEditText.text.toString()
            val reNewPassword = binding.reNewPasswordEditText.text.toString()
            val errorMessage = Validator.passwordValidator(newPassword)
            if (errorMessage != null) {
                binding.newPasswordEditTextlayout.error = errorMessage
                return@setOnClickListener
            } else {
                binding.newPasswordEditTextlayout.error = null
            }
            if (reNewPassword != newPassword) {
                binding.reNewPasswordEditTextlayout.error = "not match to new password"
                return@setOnClickListener
            } else {
                binding.reNewPasswordEditTextlayout.error = null
            }

            requireActivity().getSharedPreferences(
                "mySharePref",
                AppCompatActivity.MODE_PRIVATE
            )
                .let {
                    it.getString("userId", "")?.let { it1 ->
                        viewModel.changePassword(
                            it1.toLong(),
                            binding.currentPasswordEditText.text.toString(),
                            newPassword
                        )
                    }
                }
        }
    }

    private fun setObserve() {
        viewModel.isMatchCurrentPassword.observe(viewLifecycleOwner) { value ->
            if (value) {
                parentFragmentManager.popBackStack()
                Toast.makeText(
                    requireContext(), "Password update successfully",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                binding.currentPasswordEditTextlayout.error = "password is incorrect "
            }
        }
    }
}
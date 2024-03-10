package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.lifecycleScope
import com.application.R
import com.application.databinding.FragmentLoginBinding
import com.application.viewmodels.SignInViewModel
import com.application.viewmodels.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var  binding : FragmentLoginBinding

    private  val viewModel : SignInViewModel by activityViewModels { SignInViewModel.FACTORY}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)


         binding.signup.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_view_container,SignupFragment())
            fragmentTransaction.addToBackStack("addSignupPage")
            fragmentTransaction.commit()
        }


         binding.signin.setOnClickListener{
             lifecycleScope.launch(Dispatchers.IO){
                 Log.i("TAG",Thread.currentThread().toString())
                 Log.i("TAG",binding.email.text.toString().trim())
               val userId =  viewModel.signIn(binding.email.text.toString().trim(), binding.password.text.toString())
                 Log.i("TAG","userId $userId")
             }
        }
    }


}
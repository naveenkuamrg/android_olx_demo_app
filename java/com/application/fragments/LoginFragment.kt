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
import com.application.model.AuthenticationResult
import com.application.viewmodels.SignInViewModel
import com.application.viewmodels.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                 val result = viewModel.signIn(binding.email.text.toString().trim(),
                     binding.password.text.toString())
                 Log.i("TAG",result.toString())
                 when(result){
                     AuthenticationResult.USER_NOT_FOUND -> {
                         withContext(Dispatchers.Main){
                             binding.errorMessage.text = "User is not found"
                             binding.errorMessage.visibility = View.VISIBLE
                         }

                     }
                     AuthenticationResult.PASSWORD_INVALID ->{
                         withContext(Dispatchers.Main){
                             binding.errorMessage.text = "password invalid"
                             binding.errorMessage.visibility = View.VISIBLE
                         }

                     }
                     AuthenticationResult.LOGIN_SUCCESS ->{
                         withContext(Dispatchers.Main){
                             val homeTransaction = parentFragmentManager.beginTransaction()
                             homeTransaction.replace(R.id.main_view_container,HomeFragment())
                             homeTransaction.commit()
                         }
                     }

                 }
             }
        }
    }


}
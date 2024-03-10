package com.application.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.AuthenticationResult
import com.application.repositories.SignInRepository
import com.application.repositories.impl.SignInRepositoryImpl

class SignInViewModel(private var repository : SignInRepository) : ViewModel() {



    suspend fun  signIn(email : String , password : String): AuthenticationResult{
        if(!repository.isEmailExist(email)){
            return AuthenticationResult.USER_NOT_FOUND
        }
        if(!repository.isPasswordMatch(email,password)){
            return AuthenticationResult.PASSWORD_INVALID
        }
           return AuthenticationResult.LOGIN_SUCCESS
    }

    companion object{
        val FACTORY = object : ViewModelProvider.Factory{

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                val repository = SignInRepositoryImpl(application!!.applicationContext)

                return SignInViewModel(repository) as T
            }
        }
    }
}
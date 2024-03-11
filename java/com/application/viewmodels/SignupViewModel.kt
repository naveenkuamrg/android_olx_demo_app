package com.application.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.helper.Validator
import com.application.model.RegisterResult
import com.application.repositories.RegisterRepository
import com.application.repositories.impl.RegisterRepositoryImpl
import kotlinx.coroutines.launch

class SignupViewModel(val repository: RegisterRepository) : ViewModel() {

    suspend fun signup(name : String , email : String ,phoneNumber : String,password : String) : RegisterResult{

            if(!repository.isEmailExist(email)) {
                return RegisterResult.ALREADY_REGISTERED
            }

            repository.setUserProfile(name,email,phoneNumber,password)
        return RegisterResult.REGISTERED_SUCCESS

    }
    companion object{
        val FACTORY = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                val repository = RegisterRepositoryImpl(application!!.applicationContext)

                return SignupViewModel(repository) as T
            }
        }
    }

}
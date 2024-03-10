package com.application.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.repositories.RegisterRepository
import com.application.repositories.impl.RegisterRepositoryImpl
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: RegisterRepository) : ViewModel() {

    fun signup(name : String , email : String ,phoneNumber : String,password : String){
        viewModelScope.launch {
            repository.registerUser(name,email,phoneNumber,password)
            Log.i("TAG",Thread.currentThread().toString())
        }
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
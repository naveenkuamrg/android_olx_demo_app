package com.application.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.repositories.SignInRepository
import com.application.repositories.impl.SignInRepositoryImpl
import kotlinx.coroutines.launch

class SignInViewModel(private var repository : SignInRepository) : ViewModel() {



    suspend fun  signIn(email : String , password : String): Int{
        Log.i("TAG",repository.getUsers().toString())
        Log.i("TAG",repository.getUsers()[2].id.toString())
        Log.i("TAG password",repository.getPassword("c"))
           return repository.getUserId(email, password)
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
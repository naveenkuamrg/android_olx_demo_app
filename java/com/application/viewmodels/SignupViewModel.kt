package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.exceptions.InvalidUserDataException
import com.application.repositories.AuthenticationRepository
import com.application.repositories.impl.AuthenticationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: AuthenticationRepository) : ViewModel() {
    private var _userId = MutableLiveData<Long>()
    val userId : LiveData<Long> = _userId

    var secondAttempt : Boolean = false

    private  var _exception  = MutableLiveData<InvalidUserDataException>()
    val exception : LiveData<InvalidUserDataException> = _exception
    fun signup(name : String ,
                       email : String ,
                       phoneNumber : String,
                       password : String) {
         viewModelScope.launch(Dispatchers.IO) {
            try {
               _userId.postValue( repository.setUserProfile(name, email, phoneNumber, password))
            }catch (e : InvalidUserDataException){

                _exception.postValue(e)
            }
         }
    }
    companion object{
        val FACTORY = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                val repository = AuthenticationRepositoryImpl(application!!.applicationContext)

                return SignupViewModel(repository) as T
            }
        }
    }

}
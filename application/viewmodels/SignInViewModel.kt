package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.exceptions.AuthenticationSignInException
import com.application.model.Profile
import com.application.repositories.AuthenticationRepository
import com.application.repositories.impl.AuthenticationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    private val _user = MutableLiveData<Profile>()
    val user: LiveData<Profile> = _user

    private val _exceptions = MutableLiveData<AuthenticationSignInException>()
    val exceptions: LiveData<AuthenticationSignInException> = _exceptions

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val id = repository.getUser(email, password)
                _user.postValue(id)
            } catch (e: AuthenticationSignInException) {
                _exceptions.postValue(e)
            }
        }
    }


    companion object {
        val FACTORY = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                val repository = AuthenticationRepositoryImpl(application!!.applicationContext)
                return SignInViewModel(repository) as T
            }
        }
    }
}

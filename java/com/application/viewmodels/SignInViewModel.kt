package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.exceptions.AuthenticationSignInException
import com.application.repositories.AuthenticationRepository
import com.application.repositories.impl.AuthenticationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    private val _exceptions = MutableLiveData<AuthenticationSignInException>()
    val exceptions: LiveData<AuthenticationSignInException> = _exceptions

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val id = repository.getUserId(email, password)
                _userId.postValue(id)
            } catch (e: AuthenticationSignInException) {
                _exceptions.postValue(e)
            }
        }
    }

    fun clearUserId() {
        _userId.value = -1
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

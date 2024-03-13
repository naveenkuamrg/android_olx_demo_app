package com.application.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.repositories.UserRepository
import com.application.repositories.impl.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePasswordViewModel(val repository: UserRepository) : ViewModel() {

    private val _isMatchCurrentPassword = MutableLiveData<Boolean>()
    val isMatchCurrentPassword : LiveData<Boolean> = _isMatchCurrentPassword

    fun changePassword(id: Long,currentPassword: String,newPassword: String){
        viewModelScope.launch(Dispatchers.Default) {
            if(repository.updateUserPassword(id, currentPassword, newPassword) == 1){
                _isMatchCurrentPassword.postValue(true)
            }else{
                _isMatchCurrentPassword.postValue(false)
            }
        }
    }

    companion object {
        val FACTORY = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                val repository = UserRepositoryImpl(application!!.applicationContext)


                return ChangePasswordViewModel(repository) as T
            }
        }
    }
}
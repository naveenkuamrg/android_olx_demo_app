package com.application.viewmodels

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.exceptions.InvalidUserDataException
import com.application.model.Profile
import com.application.repositories.UserRepository
import com.application.repositories.impl.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileViewModel(private  val userRepository : UserRepository) :ViewModel(){

    private val _isUploaded = MutableLiveData<Boolean>()
    val isUploaded : LiveData<Boolean> = _isUploaded

    private val _exception = MutableLiveData<InvalidUserDataException>()
    val exception : LiveData<InvalidUserDataException> = _exception

    fun uploadProfile(name : String,email: String,phone: String,profile: Profile){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = profile.id
            var isUploaded = true
            if(name != profile.name){
                userRepository.updateUserName(name,userId)
            }
            if(email != profile.email){
                try {
                    userRepository.updateUserEmail(email,userId)
                }catch(e: SQLiteConstraintException){
                    Log.i("TAG",e.message.toString())
                    withContext(Dispatchers.Main) {
                        _exception.value = InvalidUserDataException.EmailAlreadyExists()
                    }
                    isUploaded = false
                }
            }
            if(phone != profile.phoneNumber){
                try {
                    userRepository.updateUserPhone(phone, userId)
                }catch (e: SQLiteConstraintException){
                    withContext(Dispatchers.Main) {
                        _exception.value = InvalidUserDataException.PhoneNumberAlreadyRegistered()
                    }
                    isUploaded = false
                }
            }
            if(isUploaded) {
                _isUploaded.postValue(true)
            }
        }


    }
    companion object {
        val FACTORY = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                val repository = UserRepositoryImpl(application!!.applicationContext)

                return EditProfileViewModel(repository) as T
            }
        }
    }
}
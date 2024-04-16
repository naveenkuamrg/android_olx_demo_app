package com.application.repositories.impl

import android.content.Context
import com.application.AppDatabase
import com.application.dao.UserDao
import com.application.exceptions.AuthenticationSignInException
import com.application.helper.ModelConverter
import com.application.model.Profile
import com.application.repositories.AuthenticationRepository

class AuthenticationRepositoryImpl(context : Context) : AuthenticationRepository {


    private val userDao : UserDao = AppDatabase.getInstance(context).userDao
    override suspend fun setUserProfile(
        name: String,
        email: String,
        phoneNumber: String,
        password: String
    ) : Long {
        userDao.insertUser(
            ModelConverter.buildUserEntityFromUserDetails(name, phoneNumber, email, password)
        )
        return  userDao.getUserId(email)
    }

    override suspend fun getUser(identifier: String, password: String): Profile {
       val user = userDao.getUser(identifier) ?: throw AuthenticationSignInException.
            UserNotFoundAuthenticationException("User not found")
        if(user.password != password){
            throw  AuthenticationSignInException.
                PasswordInvalidAuthenticationException("Invalid password",)
        }
        return  ModelConverter.userEntityToProfile(user)
    }

}
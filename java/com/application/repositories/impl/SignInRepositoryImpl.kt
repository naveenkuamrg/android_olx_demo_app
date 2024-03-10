package com.application.repositories.impl

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Email
import com.application.AppDatabase
import com.application.entity.User
import com.application.repositories.SignInRepository

class SignInRepositoryImpl(context : Context) : SignInRepository {
    val userDao = AppDatabase.getInstance(context).userDao


    override suspend fun getUsers(): List<User> {
        return  userDao.getProfile()
    }


    override suspend fun getPassword(email: String): String {
       return  userDao.getUserPassword(email)
    }
    override suspend fun getUserId(email: String, password: String): Int {
        return  userDao.getUserId(email)
    }


}
package com.application.repositories

import com.application.model.RegisterResult

interface RegisterRepository{
    suspend fun setUserProfile(name:String,email:String,phoneNumber:String,password:String)

     suspend fun isEmailExist(email: String): Boolean

}
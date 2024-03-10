package com.application.repositories

import com.application.model.AuthenticationResult

interface RegisterRepository{
    suspend fun registerUser(name:String,email:String,phoneNumber:String,password:String)  : AuthenticationResult



}
package com.application.repositories

import com.application.model.RegisterResult

interface RegisterRepository{
    suspend fun registerUser(name:String,email:String,phoneNumber:String,password:String)  : RegisterResult



}
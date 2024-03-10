package com.application.helper

import com.application.entity.User

object ModelConverter {
    fun UserEntityFromUserDetails(name:String,phoneNumber: String,email: String,password: String): User{
        return User(
            name,
            phoneNumber,
            email,
            password
        )
    }
}



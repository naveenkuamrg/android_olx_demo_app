package com.application.helper

import android.net.Uri
import com.application.entity.User
import com.application.model.Profile

object ModelConverter {
    fun userEntityFromUserDetails(name:String, phoneNumber: String, email: String, password: String): User{
        return User(
            name,
            phoneNumber,
            email,
            password
        )
    }
    fun profileFromUserAndUri(user: User, uri : Uri):Profile{
        return Profile(user.id,user.name,uri,user.phoneNumber,user.email)
    }
}



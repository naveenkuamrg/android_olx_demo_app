package com.application.helper

import android.graphics.Bitmap
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
    fun profileFromUserAndUri(user: User):Profile{
        return Profile(user.id,user.name,null,user.phoneNumber,user.email)
    }
}



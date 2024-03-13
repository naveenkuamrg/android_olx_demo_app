package com.application.model

import android.graphics.Bitmap
import android.net.Uri


data class Profile(
    val id: Long,
    val name: String,
    var profileImage : Bitmap?,
    val phoneNumber: String,
    val email: String
)





















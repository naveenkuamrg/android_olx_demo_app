package com.application.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Ignore

data class ProfileSummary(
    val userId: Long,
    val name: String,
){
    @Ignore
    lateinit var profileImage: Uri
}
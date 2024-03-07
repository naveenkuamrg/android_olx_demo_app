package com.application.model

import android.graphics.Bitmap
import android.net.Uri

data class ProfileSummary(
    val userId: Long,
    val name: String,
    val profileImage: Uri
)
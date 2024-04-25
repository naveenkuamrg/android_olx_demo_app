package com.application.model

import android.graphics.Bitmap

data class ProfileSummary(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val isContented: Boolean,
    var profileImage: Bitmap? = null
)
package com.application.callbacks

import android.graphics.Bitmap

interface PhotoPickerBottomSheet {
    fun getBitmapCount(): Int
    fun addBitmap(bitmap: Bitmap)
}
package com.application.callbacks

import android.graphics.Bitmap

interface BottomSheetDialogPhotoPicker {
    fun getCountOfBitmapList(): Int
    fun setBitmap(bitmap: Bitmap)
}
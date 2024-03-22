package com.application.callbacks

import android.graphics.Bitmap

interface ProductImageViewBackgroundFragmentCallBack {
    fun getCountOfBitmapList(): Int
    fun setBitmap(bitmap: Bitmap)
}
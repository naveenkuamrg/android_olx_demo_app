package com.application.callbacks

import android.graphics.Bitmap

interface ImageAdapterListener {
    fun onRemoveButtonClick(element: Bitmap)
}
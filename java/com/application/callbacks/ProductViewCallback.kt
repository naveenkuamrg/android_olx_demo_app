package com.application.callbacks

interface ProductViewCallback {
    fun onShowProductEditDetailPage()
    fun onShowProductDetailsPage(productId: Long, isCurrentUser:Boolean)
}


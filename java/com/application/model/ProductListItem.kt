package com.application.model

import android.graphics.Bitmap

import androidx.room.Ignore


sealed class ProductListItem {
    abstract var id: Long

    data class ProductItem(
        override var id: Long,
        val title: String,
        val postedDate: String,
        val location: String,
        val price: String,

        ) : ProductListItem() {
        @Ignore
        var image: Bitmap? = null
    }

     object Header: ProductListItem(){
        override var id = Long.MIN_VALUE
    }


}




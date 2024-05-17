package com.application.model

import android.graphics.Bitmap

import androidx.room.Ignore


sealed class ProductListItem {
    abstract var id: Long

    data class ProductItem(
        override var id: Long,
        val title: String,
        val postedDate: Long,
        val location: String,
        val price: Double,
        val availabilityStatus: AvailabilityStatus
        ) : ProductListItem() {
        @Ignore
        lateinit var image: Bitmap
    }

     class Header: ProductListItem(){
        override var id = Long.MIN_VALUE
    }

    class Divider(val title: String): ProductListItem(){
        override var id: Long = Long.MIN_VALUE
    }



}




package com.application.helper

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date

object Utility {

    @JvmStatic
    fun convertToINR(price: Double): String {
        return "RS: $price"
    }

    @JvmStatic
    fun convertToString(price: Double): String{
        if(price == 0.0){
            return ""
        }
        return  price.toString()
    }

    @JvmStatic
    fun millisecondsToString(milliseconds: Long): String {
        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val date = Date(milliseconds)
        val sdf = SimpleDateFormat(dateFormat)
        return sdf.format(date)
    }

}
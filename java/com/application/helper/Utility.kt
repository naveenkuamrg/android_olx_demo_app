package com.application.helper

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.FieldPosition
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utility {

    @JvmStatic
    fun convertToINR(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        return format.format(price)
    }

    @JvmStatic
    fun convertToString(price: Double): String {
        if (price == 0.0) {
            return ""
        }
        return String.format("%.2f", price)
    }

    @JvmStatic
    fun millisecondsToString(milliseconds: Long): String {
        val dateFormat = "dd-MM-yyyy"
        val date = Date(milliseconds)
        val sdf = SimpleDateFormat(dateFormat)
        return sdf.format(date)
    }


    fun createAlertDialog(
        context: Context,
        message: String,
        positionCallback: (DialogInterface, Int) -> Unit,
        negativeCallback: (DialogInterface, Int) -> Unit
    ) {
        AlertDialog.Builder(context).apply {
            setMessage(message)
            setPositiveButton("Yes", positionCallback)
            setNegativeButton("No", negativeCallback)
            show()
        }

    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

}
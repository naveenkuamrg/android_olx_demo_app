package com.application.helper

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.application.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


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



    fun getLoginUserId(context: Context): Long {
       return context.getSharedPreferences("mySharePref", AppCompatActivity.MODE_PRIVATE)
           .getString("userId", "-1")?.toLong() ?: -1
    }

    fun getLoginUserName(context: Context): String{
        return context.getSharedPreferences("mySharePref",AppCompatActivity.MODE_PRIVATE)
            .getString("userName","") ?: ""
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setCreatedTime(createdTime : Long) : String{
        val currentTime = Date()
        val durationInMillis = currentTime.time -  createdTime
        val days = TimeUnit.MILLISECONDS.toDays(durationInMillis).toInt()
        val hours = TimeUnit.MILLISECONDS.toHours(durationInMillis).toInt()
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis).toInt()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis).toInt()
        if(days==0){
            return if(hours==0){
                if(minutes == 0){
                    if(seconds == 0){
                        "Just now"
                    } else{
                        "$seconds ${if(seconds==1) "second" else "seconds"} ago"
                    }
                } else{
                    "$minutes ${if(minutes==1) "minute" else "minutes"} ago"
                }
            } else{
                "$hours ${if(hours==1) "hour" else "hours"} ago"
            }
        }
        else if(days<7){
            return  "$days ${if(days==1) "day" else "days"} ago"
        }
        else if(days in 7..14){
            return  "1 week ago"
        }
        else{
            val month = getMonthStringFromDate(createdTime)
            val year = getYearFromDate(createdTime)
            val day = getDayFromDate(createdTime)

            val currentYear = getYearFromDate(currentTime.time)


            return if(currentYear == year){
                "$day $month"
            } else{
                "$day $month $year"
            }
        }
    }

    private fun getYearFromDate(date: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.time.time = date
        return calendar.get(Calendar.YEAR)
    }

    private fun getMonthStringFromDate(date: Long): String {
        val calendar = Calendar.getInstance()
        calendar.time.time = date
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
    }


    private fun getDayFromDate(date: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.time.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /*
    This function commit and animation when the fragment is replace and commit
     */
    fun FragmentManager.commitWithSlideAnimation(
        addToBackStack: String?,
        fragment: Fragment,
        containerViewId: Int
    ) {
        commit{
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in_pop,
                R.anim.slide_out_pop
            )
            replace(containerViewId, fragment)
            if (addToBackStack != null) {
                addToBackStack(addToBackStack)
            }
        }
    }

    fun removeErrorAfterTextChanged(textView: TextInputEditText, textLayout: TextInputLayout) {
        textView.doAfterTextChanged {
            textLayout.error = ""
        }
    }

}
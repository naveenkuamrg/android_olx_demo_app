package com.application.helper

import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern


object Validator {
    fun isEmailValid(email : String) : Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }

    fun isPhoneNumberValid(phone : String) : Boolean {
       return phone.length == 10
    }

    fun doesNotContainSpecialChars(text: String): Boolean {
        val regex = Regex("^[a-zA-Z\\s]+$")
        return regex.matches(text)
    }

     fun passwordValidator(password: String): String? {

        var error : String? = null

        val lowercaseRegex = "(?=.*[a-z])"
        val uppercaseRegex = "(?=.*[A-Z])"
        val digitRegex = "(?=.*\\d)"
        val specialCharRegex = "(?=.*[@$!%*?&])"
        val lengthRegex = ".{8,}"
        val regex = lowercaseRegex + uppercaseRegex + digitRegex + specialCharRegex + lengthRegex
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(password)
        if (!matcher.matches()) {
            error = "Password must contain: \n"
            if (!password.matches("$lowercaseRegex.*".toRegex())) {
                error += "at least one lowercase letter \n"
            }
            if (!password.matches("$uppercaseRegex.*".toRegex())) {
                error += "at least one uppercase letter \n "
            }
            if (!password.matches("$digitRegex.*".toRegex())) {
                error += "at least one digit\n"
            }
            if (!password.matches("$specialCharRegex.*".toRegex())) {
                error += "at least one special character\n"
            }
            if (!password.matches(lengthRegex.toRegex())) {
                error += "be at least 8 characters long\n"
            }
        }
        return error
    }

    fun priceValidation(price: Double): String?{
        if(price > 9999990){
            return  "maximum amount to quotes is 9999990"
        }
        return  null
    }
}
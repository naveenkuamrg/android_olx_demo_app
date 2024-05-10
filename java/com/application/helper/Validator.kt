package com.application.helper

import android.widget.TextView
import com.application.model.ProductType
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Matcher
import java.util.regex.Pattern


object Validator {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }

    fun isPhoneNumberValid(phone: String): Boolean {
        return phone.length == 10
    }

    fun doesNotContainSpecialChars(text: String): Boolean {
        val regex = Regex("^[a-zA-Z\\s]+$")
        return regex.matches(text)
    }

    fun passwordValidator(password: String): String? {

        var error: String? = null

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
                error += "at least one uppercase letter \n"
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


    fun validateField(
        value: String,
        handler: (Boolean) -> Unit
    ) {
        if (value.isEmpty()) {
            handler(false)
        } else {
            handler(true)
        }
    }

    fun  validatePrice(price: String,errorHandler: (Boolean,errorMessage: String?) -> Unit) {
        val _price = price.toDoubleOrNull()
        if(_price == null){
            errorHandler(false,"Price should not be empty")
            return
        }
        if  (_price > 100000000) {
            errorHandler(false,"Your price should be less than Rs 10,00,00,000")
        } else {
            errorHandler(true,null)

        }
    }

    fun validateCategory(category: String,handler: (Boolean) -> Unit) {
         if (ProductType.stringToProductType(category) == null) {
             handler(false)
        } else {
            handler(true)
        }
    }

    fun validateImages(imagesSize: Int, errorView: TextView, handler: (Boolean) -> Unit) {
         if (imagesSize == 0) {
            errorView.text = "Must upload a single image"
           handler(false)
        } else {
            handler(true)
            errorView.text = ""
        }
    }
}
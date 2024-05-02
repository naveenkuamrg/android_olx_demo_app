package com.application.helper

import android.text.Editable

object StringConverter {
    fun toEditable(string: String): Editable{
        return  Editable.Factory.getInstance().newEditable(string)
    }
}
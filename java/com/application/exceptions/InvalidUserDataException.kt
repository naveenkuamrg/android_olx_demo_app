package com.application.exceptions

sealed class InvalidUserDataException(message: String) : Exception(message) {
    class EmailAlreadyExists : InvalidUserDataException("This email already exits")
    class PhoneNumberAlreadyRegistered :
        InvalidUserDataException("This Phone number already register")
}

package com.application.exceptions


sealed class AuthenticationSignInException(message: String) : Exception(message){
    class UserNotFoundAuthenticationException(message: String) : AuthenticationSignInException(message)
    class PasswordInvalidAuthenticationException(message: String): AuthenticationSignInException(message)
}

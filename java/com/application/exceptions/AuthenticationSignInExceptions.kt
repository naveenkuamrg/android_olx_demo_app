package com.application.exceptions


sealed class AuthenticationSignInExceptions(message: String) : Exception(message){
    class UserNotFoundAuthenticationException(message: String) : AuthenticationSignInExceptions(message)
    class PasswordInvalidAuthenticationException(message: String): AuthenticationSignInExceptions(message)
}

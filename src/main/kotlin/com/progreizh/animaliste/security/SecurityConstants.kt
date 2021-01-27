package com.progreizh.animaliste.security

class SecurityConstants {
    companion object {
        const val SECRET = "SECRET_KEY"
        const val EXPIRATION_TIME = 900000 // 15 mins
        const val TOKEN_PREFIX = "Wouf"
        const val HEADER_STRING = "Authorization"
        const val SIGN_UP_URL = "/users/register"
        const val LOGIN_URL = "/users/login"
    }
}
package com.progreizh.animaliste.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.progreizh.animaliste.security.SecurityConstants
import com.progreizh.animaliste.security.SecurityConstants.Companion.EXPIRATION_TIME
import com.progreizh.animaliste.security.SecurityConstants.Companion.TOKEN_PREFIX
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenService {
    fun generateToken(auth: Authentication): String {
        val algorithm: Algorithm = Algorithm.HMAC512(SecurityConstants.SECRET)
        val subject : String = if(auth.principal is User) (auth.principal as User).username else auth.principal as String
        val token: String = JWT.create()
            .withSubject(subject)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(algorithm)
        return "$TOKEN_PREFIX$token"
    }
    fun generateToken(mail: String, password: String): String {
        val auth : Authentication = UsernamePasswordAuthenticationToken(mail,password)
        return generateToken(auth)
    }
}
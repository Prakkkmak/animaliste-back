package com.progreizh.animaliste.security.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.progreizh.animaliste.dtos.UserCredentialsDto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.io.IOException
import java.util.*

import javax.servlet.FilterChain

import com.progreizh.animaliste.security.SecurityConstants
import com.progreizh.animaliste.security.SecurityConstants.Companion.EXPIRATION_TIME
import com.progreizh.animaliste.security.SecurityConstants.Companion.LOGIN_URL
import com.progreizh.animaliste.security.SecurityConstants.Companion.SECRET
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException

// https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/

class JwtAuthenticationFilter(authenticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    init {
        this.authenticationManager = authenticationManager
        setFilterProcessesUrl(LOGIN_URL);
    }

    @Throws(IOException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val credentials: UserCredentialsDto = ObjectMapper().readValue(request.inputStream, UserCredentialsDto::class.java)

            return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    credentials.mail,
                    credentials.password,
                    ArrayList()
                )
            )
        } catch (e : IOException) {
            throw RuntimeException(e);
        } catch (e: BadCredentialsException){
            print(e.stackTrace)
            throw e
        }
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication
    ) {
        val token: String = JWT.create()
            .withSubject((auth.principal as User).username)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.toByte().toString()))
        val body = (auth.principal as User).username + " " + token
        res.writer.write(body)
        res.writer.flush()
    }
}
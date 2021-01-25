package com.progreizh.animaliste.security

import com.progreizh.animaliste.security.SecurityConstants.Companion.HEADER_STRING
import com.progreizh.animaliste.security.SecurityConstants.Companion.TOKEN_PREFIX
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import javax.servlet.ServletException

import java.io.IOException

import javax.servlet.FilterChain

import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest
import java.util.ArrayList

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.progreizh.animaliste.security.SecurityConstants.Companion.SECRET
import org.springframework.security.core.GrantedAuthority


class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        req: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = req.getHeader(HEADER_STRING)
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, response)
            return
        }
        val authentication: UsernamePasswordAuthenticationToken? = getAuthentication(req)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, response)
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(HEADER_STRING)
        if (token != null) {
            // parse the token.
            val user = JWT.require(HMAC512(SECRET))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .subject
            if (user != null) {
                // new arraylist means authorities
                return UsernamePasswordAuthenticationToken(user, null, ArrayList())
            }
        }
        return null
    }
}
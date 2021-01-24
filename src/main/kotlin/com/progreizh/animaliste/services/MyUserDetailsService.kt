package com.progreizh.animaliste.services

import com.progreizh.animaliste.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class MyUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val res = userRepository.findUserByMail(username)
        if(!res.isPresent){
            throw UsernameNotFoundException("Username not found")
        }
        else {
            val myUser = res.get()
            val grantedAuthorities = arrayListOf(SimpleGrantedAuthority("ADMIN"))
            return User(
                myUser.mail,
                myUser.password,
                grantedAuthorities
            )
        }
    }
}
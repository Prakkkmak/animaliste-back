package com.progreizh.animaliste.config
import com.progreizh.animaliste.services.MyUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import java.lang.Exception
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder





@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    // Pour les autorisations etc ce sera ici: https://www.marcobehler.com/guides/spring-security
    // https://bezkoder.com/spring-boot-vue-js-authentication-jwt-spring-security/
    // https://stackoverflow.com/questions/64960385/how-can-i-setup-login-with-spring-security-and-vue-js
    // https://medium.com/@xoor/jwt-authentication-service-44658409e12c
    
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/", "/home", "/login").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
        .logout()
            .permitAll()
            .and()
        .httpBasic()
    }

    @Bean
    override fun userDetailsService(): UserDetailsService? {
        return MyUserDetailsService()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}
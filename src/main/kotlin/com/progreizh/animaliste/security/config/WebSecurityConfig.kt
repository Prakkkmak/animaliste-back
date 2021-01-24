package com.progreizh.animaliste.security.config
import com.progreizh.animaliste.security.JwtAuthorizationFilter
import com.progreizh.animaliste.security.SecurityConstants.Companion.LOGIN_URL
import com.progreizh.animaliste.security.SecurityConstants.Companion.SIGN_UP_URL
import com.progreizh.animaliste.services.MyUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import java.lang.Exception
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity(debug = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    // Pour les autorisations etc ce sera ici: https://www.marcobehler.com/guides/spring-security
    // https://bezkoder.com/spring-boot-vue-js-authentication-jwt-spring-security/
    // https://stackoverflow.com/questions/64960385/how-can-i-setup-login-with-spring-security-and-vue-js
    // https://medium.com/@xoor/jwt-authentication-service-44658409e12c
    // https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/

    override fun configure(http: HttpSecurity) {
        /*http.cors().and()
            .authorizeRequests()
            .antMatchers(LOGIN_URL).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(JwtAuthenticationFilter(authenticationManager()))
            .addFilter(JwtAuthorizationFilter(authenticationManager()))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
        http
            .authorizeRequests()
                .antMatchers("/users/login").permitAll()
                .anyRequest().authenticated()
                .and()
            .addFilter(JwtAuthenticationFilter(authenticationManager()))
            .addFilter(JwtAuthorizationFilter(authenticationManager()))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder())
    }

    @Bean
    override fun userDetailsService(): UserDetailsService? {
        return MyUserDetailsService()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration().applyPermitDefaultValues()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }
}
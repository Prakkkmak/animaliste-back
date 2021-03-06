package com.progreizh.animaliste

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.config.annotation.CorsRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@CrossOrigin
@SpringBootApplication
class AnimalisteApplication{
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:8080")
                    .allowedMethods("GET", "PUT", "DELETE", "POST")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<AnimalisteApplication>(*args)
}




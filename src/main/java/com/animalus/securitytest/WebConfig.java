package com.animalus.securitytest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//
// WARNING: DO NOT USE THIS TAG (@EnableWebMvc). It makes the times and dates come out in arrays instead of strings.
// If you need to in the future then you will need to figure out how to set it back such that that doesn't happen.
//
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST")
            .allowCredentials(true)
            .allowedOrigins("http://localhost:3000");
    }
}

package com.backend.vanat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://vanat-crochets.com", "http://localhost:3000") // Add your Vercel URL later
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
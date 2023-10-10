package com.clicks.fulafiaquizapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] methods = {"POST", "GET", "PUT", "DELETE"};
        registry.addMapping("*")
                .allowedOrigins("*")
                .allowedMethods(methods);
    }
}

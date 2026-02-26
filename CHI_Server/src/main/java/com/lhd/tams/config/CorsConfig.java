package com.lhd.tams.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/cancle/**")
                .allowedOrigins("http://localhost:12011")
                .allowedMethods("POST")
                .allowedHeaders("Content-Type")
                .allowCredentials(true);
    }
}
package com.usama.calculatorproblemv2;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CqrsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Adjust the mapping pattern as per your requirements
            .allowedOrigins("http://localhost:3000") // Add the allowed origins
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Add the allowed HTTP methods
            .allowedHeaders("*"); // Allow all headers or specify specific ones
    }
}

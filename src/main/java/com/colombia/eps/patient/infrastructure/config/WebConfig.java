package com.colombia.eps.patient.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class WebConfig {
    /**
     * Configuración CORS para ambiente local (desarrollo)
     * Permite peticiones desde localhost:4200 y extensiones de Chrome
     */
    @Bean
    @Profile("local")
    public CorsFilter corsFilterLocal() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "http://localhost:8080",
                "chrome-extension://flnheeellpciglgpaodhkhmapeljopja"
        ));

        config.setAllowedMethods(Arrays.asList(
                "POST",
                "GET",
                "OPTIONS"
        ));

        config.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "X-Patient-Service",
                "X-User-Service",
                "X-Order-Service",
                "X-Product-Service",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }


    /**
     * Configuración CORS para ambientes NO locales (staging, prod)
     * Permite peticiones desde AWS Lambda y API Gateway
     */
    @Bean
    @Profile("!local")
    public CorsFilter corsFilterProd() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        config.setAllowedOriginPatterns(Arrays.asList(
                "*.amazonaws.com",
                "*.execute-api.*.amazonaws.com",
                "https://*.amplifyapp.com"
        ));

        config.setAllowedMethods(Arrays.asList(
                "POST",
                "GET",
                "OPTIONS"
        ));

        config.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "X-Patient-Service",
                "x-patient-service",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        config.setExposedHeaders(Arrays.asList(
                "Content-Disposition",
                "Access-Control-Allow-Origin"
        ));

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
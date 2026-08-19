package com.armalora.user.config;

import com.armalora.user.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // REST API does not use CSRF
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // Public registration
                        .requestMatchers(
                                "/api/auth/register"
                        ).permitAll()

                        // Public login
                        .requestMatchers(
                                "/api/auth/login"
                        ).permitAll()

                        // Health check
                        .requestMatchers(
                                "/actuator/health"
                        ).permitAll()

                        // Everything else requires JWT
                        .anyRequest()
                        .authenticated()
                )

                // Add our JWT filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
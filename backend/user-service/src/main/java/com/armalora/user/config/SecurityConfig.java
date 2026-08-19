package com.armalora.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
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

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
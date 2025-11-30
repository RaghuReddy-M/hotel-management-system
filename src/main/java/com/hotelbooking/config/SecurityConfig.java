package com.hotelbooking.config;

import com.hotelbooking.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Public
                .requestMatchers("/api/auth/**").permitAll()

                // GET (customer + admin)
                .requestMatchers(HttpMethod.GET, "/api/hotels/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/rooms/**").authenticated()

                // ADMIN for hotel
                .requestMatchers(HttpMethod.POST, "/api/hotels/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/hotels/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/hotels/**").hasAuthority("ADMIN")

                // ADMIN for room
                .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasAuthority("ADMIN")

                // Customer + Admin
                .requestMatchers("/api/bookings/**").authenticated()

                // All other requests
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

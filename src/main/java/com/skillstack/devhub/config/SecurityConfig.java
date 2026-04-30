package com.skillstack.devhub.config;


import com.skillstack.devhub.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
    http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/auth/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/questions").permitAll()
                    .requestMatchers(HttpMethod.POST, "/questions").permitAll()
                    .requestMatchers(HttpMethod.POST, "/questions/*/answer").permitAll()
                    .requestMatchers(HttpMethod.GET, "/questions/*/reviews").permitAll()
                    .requestMatchers(HttpMethod.POST, "/questions/*/reviews").permitAll()
                    .requestMatchers("/questions/**").permitAll()
                    
                    .requestMatchers(HttpMethod.GET, "/comments/top").permitAll()
                    .requestMatchers(HttpMethod.GET, "/comments/starred").permitAll()
                    .requestMatchers(HttpMethod.GET, "/comments/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/comments").authenticated()
                    .requestMatchers(HttpMethod.POST, "/comments/**").authenticated()

                    .requestMatchers(HttpMethod.GET, "/user/ranking").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}

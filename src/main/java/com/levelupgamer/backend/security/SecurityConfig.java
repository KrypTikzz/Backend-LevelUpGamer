package com.levelupgamer.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // desactivar csrf porque es una api rest
        http.csrf(csrf -> csrf.disable());

        // permitir peticiones desde el frontend
        http.cors(Customizer.withDefaults());

        // configuracion de acceso a rutas
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()       // registro y login libres
                .requestMatchers("/api/productos/**").permitAll()  // productos libres
                .anyRequest().authenticated()                      // lo demás requiere token
        );

        // registrar el filtro que valida el jwt antes de que se procesen las rutas
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

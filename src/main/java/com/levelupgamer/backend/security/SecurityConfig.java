package com.levelupgamer.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> auth
                // 1. Rutas públicas
                .requestMatchers("/api/auth/**").permitAll()

                // 2. Lectura pública (Catálogo y Categorías)
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()

                // 3. Rutas SOLO para ADMIN (Crear/Borrar Productos, Usuarios, Categorías)
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasAuthority("ADMIN")

                // ✅ Permitir que cualquier usuario logueado vea un perfil específico (necesario para "Mi Perfil")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}").authenticated()
                .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")

                // 4. El resto requiere al menos estar logueado (ej: Pedidos, Perfil propio)
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
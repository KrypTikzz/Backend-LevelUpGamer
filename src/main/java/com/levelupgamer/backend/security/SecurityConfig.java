package com.levelupgamer.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // desactivar csrf porque es api rest
        http.csrf(AbstractHttpConfigurer::disable);

        http.cors(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> auth
                // 1) Swagger totalmente libre (para probar en navegador)
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()

                // 2) Rutas públicas de auth (registro / login)
                .requestMatchers("/api/auth/**").permitAll()

                // 3) Lectura pública del catálogo y categorías
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()

                // 4) Operaciones SOLO ADMIN sobre productos
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ADMIN")

                // 5) Operaciones SOLO ADMIN sobre categorías
                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasAuthority("ADMIN")

                // 6) Usuarios:
                //    - cualquier logueado puede hacer GET (por ejemplo "Mi perfil")
                //    - el resto de acciones sobre usuarios solo ADMIN
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()
                .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")

                // 7) Cualquier otra ruta (pedidos, etc.) requiere estar logueado
                .anyRequest().authenticated()
        );

        // filtro que lee el JWT y mete el usuario al contexto de seguridad
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

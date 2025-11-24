package com.levelupgamer.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    // clave para firmar tokens (mínimo 32 caracteres)
    private final String claveSecreta = "ClaveJWTLevelUpGamerSuperSegura123456789";

    // expiración: 5 horas
    private final long tiempoExpiracion = 1000 * 60 * 60 * 5;

    // generar token con correo y rol
    public String generarToken(String correo, String rol) {

        return Jwts.builder()
                .setSubject(correo)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tiempoExpiracion))
                .signWith(Keys.hmacShaKeyFor(claveSecreta.getBytes()))
                .compact();
    }

    // obtener claims del token
    public Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(claveSecreta.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // comprobar si el token es valido
    public boolean tokenValido(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // obtener correo desde el token
    public String obtenerCorreo(String token) {
        return obtenerClaims(token).getSubject();
    }

    // obtener rol desde el token
    public String obtenerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }
}

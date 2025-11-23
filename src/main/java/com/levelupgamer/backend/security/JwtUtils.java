package com.levelupgamer.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    // clave para firmar los tokens, debe tener al menos 32 caracteres
    private final String claveSecreta = "ClaveJWTLevelUpGamerSuperSegura123456789";

    // tiempo de expiración del token (5 horas)
    private final long tiempoExpiracion = 1000 * 60 * 60 * 5;

    // genera un token nuevo usando el correo y el rol del usuario
    public String generarToken(String correo, String rol) {

        return Jwts.builder()
                .setSubject(correo)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tiempoExpiracion))
                .signWith(Keys.hmacShaKeyFor(claveSecreta.getBytes()))
                .compact();
    }

    // obtiene el correo desde el token
    public String obtenerCorreo(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(claveSecreta.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

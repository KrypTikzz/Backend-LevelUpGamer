package com.levelupgamer.backend.services;

import com.levelupgamer.backend.dtos.*;
import com.levelupgamer.backend.models.Rol;
import com.levelupgamer.backend.models.entities.Usuario;
import com.levelupgamer.backend.repositories.UsuarioRepository;
import com.levelupgamer.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtils jwtUtils;

    public AuthResponse registro(RegisterRequest request) {

        // validar regla de negocio
        if (request.getEdad() == null || request.getEdad() < 18) {
            throw new IllegalArgumentException("debes tener al menos 18 años para registrarte");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("el correo ya está registrado");
        }


        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre());
        nuevo.setApellido(request.getApellido());
        nuevo.setCorreo(request.getCorreo());
        nuevo.setContrasena(request.getContrasena());
        nuevo.setEdad(request.getEdad());
        nuevo.setRol(Rol.CLIENTE);

        usuarioRepository.save(nuevo);

        String token = jwtUtils.generarToken(nuevo.getCorreo(), nuevo.getRol().name());

        return new AuthResponse(
                token,
                nuevo.getId(),
                nuevo.getNombre() + " " + nuevo.getApellido(),
                nuevo.getCorreo(),
                nuevo.getRol()
        );
    }

    public AuthResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByCorreo(request.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("credenciales incorrectas"));

        if (!usuario.getContrasena().equals(request.getContrasena())) {
            throw new IllegalArgumentException("credenciales incorrectas");
        }

        String token = jwtUtils.generarToken(usuario.getCorreo(), usuario.getRol().name());

        return new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getRol()
        );
    }
}

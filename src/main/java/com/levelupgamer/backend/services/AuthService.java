package com.levelupgamer.backend.services;

import com.levelupgamer.backend.dtos.AuthResponse;
import com.levelupgamer.backend.dtos.LoginRequest;
import com.levelupgamer.backend.dtos.RegisterRequest;
import com.levelupgamer.backend.models.Rol;
import com.levelupgamer.backend.models.entities.Usuario;
import com.levelupgamer.backend.repositories.UsuarioRepository;
import com.levelupgamer.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtils jwtUtils;

    public AuthResponse registro(RegisterRequest request) {

        // validar fecha de nacimiento
        if (request.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("debes ingresar tu fecha de nacimiento");
        }

        // calcular edad
        LocalDate nacimiento = request.getFechaNacimiento();
        int edad = Period.between(nacimiento, LocalDate.now()).getYears();

        if (edad < 18) {
            throw new IllegalArgumentException("debes tener al menos 18 años para registrarte");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("el correo ya está registrado");
        }

        // crear usuario
        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre());
        nuevo.setApellido(request.getApellido());
        nuevo.setCorreo(request.getCorreo());
        nuevo.setContrasena(request.getContrasena());

        // nuevos campos opcionales
        nuevo.setTelefono(request.getTelefono());
        nuevo.setRegion(request.getRegion());
        nuevo.setComuna(request.getComuna());
        nuevo.setFechaNacimiento(request.getFechaNacimiento());

        // por defecto todos los registros desde web son CLIENTE
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

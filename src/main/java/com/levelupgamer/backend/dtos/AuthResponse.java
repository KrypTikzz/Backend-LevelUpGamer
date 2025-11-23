package com.levelupgamer.backend.dtos;

import com.levelupgamer.backend.models.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long idUsuario;
    private String nombreCompleto;
    private String correo;
    private Rol rol;
}

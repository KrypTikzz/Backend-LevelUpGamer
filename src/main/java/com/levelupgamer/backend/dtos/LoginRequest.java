package com.levelupgamer.backend.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String contrasena;
}

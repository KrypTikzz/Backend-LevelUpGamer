package com.levelupgamer.backend.dtos;

import lombok.Data;

@Data
public class RegisterRequest {

    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private Integer edad;
}

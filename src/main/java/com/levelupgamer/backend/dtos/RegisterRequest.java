package com.levelupgamer.backend.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    // Eliminamos 'edad' y usamos fechaNacimiento que es más preciso
    private LocalDate fechaNacimiento;
    private String telefono;
    private String region;
    private String comuna;
}
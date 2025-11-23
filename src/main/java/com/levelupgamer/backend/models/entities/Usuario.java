package com.levelupgamer.backend.models.entities;

import com.levelupgamer.backend.models.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email
    @Column(unique = true)
    private String correo;

    @NotBlank
    private String contrasena;

    // ✅ Nuevos campos para igualar a la Web
    private String telefono;
    private String region;
    private String comuna;
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.CLIENTE;
}
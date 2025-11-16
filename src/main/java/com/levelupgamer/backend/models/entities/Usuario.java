package com.levelupgamer.backend.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Usuario para el backend de Level Up Gamer.
 * Representa a los mismos usuarios que tienes en la app móvil:
 * nombre, apellido, correo, contraseña, edad y si es administrador.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @Email(message = "El correo no tiene un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String contrasena;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad es demasiado alta")
    private Integer edad;

    /**
     * Indica si el usuario tiene rol de administrador.
     * Por defecto es false.
     */
    @Column(nullable = false)
    private Boolean admin = false;
}

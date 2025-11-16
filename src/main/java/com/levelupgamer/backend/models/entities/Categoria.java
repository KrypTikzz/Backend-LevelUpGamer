package com.levelupgamer.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la categoría. Se expone como "nombreCategoria" para alinear
     * los nombres con la versión móvil. La columna en la base de datos
     * permanece como "nombre".
     */
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombreCategoria;

    /**
     * Descripción de la categoría. La columna original "descripcion" se mantiene.
     */
    @Column(name = "descripcion", length = 255)
    private String descripcionCategoria;
}

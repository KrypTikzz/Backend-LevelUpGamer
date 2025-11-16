package com.levelupgamer.backend.dtos;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    /**
     * Nombre de la categoría. Coincide con el naming de la app móvil.
     */
    private String nombreCategoria;

    /**
     * Descripción de la categoría.
     */
    private String descripcionCategoria;
}

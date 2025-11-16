package com.levelupgamer.backend.dtos;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    /**
     * Nombre del producto. Utilizamos el mismo nombre de propiedad que en la
     * entidad de productos de la app móvil ("nombreProducto").
     */
    private String nombreProducto;

    /**
     * Descripción del producto.
     */
    private String descripcionProducto;

    /**
     * Precio unitario del producto.
     */
    private Double precioProducto;

    /**
     * URL de la imagen asociada al producto.
     */
    private String imagenUrl;

    /**
     * Cantidad disponible en stock. Se ajusta al nombre de la app móvil.
     */
    private Integer cantidadDisponible;

    /**
     * Identificador de la categoría a la que pertenece este producto.
     */
    private Long categoriaId;

    /**
     * Nombre de la categoría del producto. En la app móvil se representa
     * como "categoriaProducto", por lo que usamos el mismo nombre.
     */
    private String categoriaProducto;
}


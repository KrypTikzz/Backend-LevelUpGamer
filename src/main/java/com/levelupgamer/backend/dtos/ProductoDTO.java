package com.levelupgamer.backend.dtos;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagenUrl;
    private Integer stockDisponible;
    private Long categoriaId;
    private String categoriaNombre;
}


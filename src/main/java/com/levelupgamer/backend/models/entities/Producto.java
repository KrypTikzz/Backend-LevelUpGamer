package com.levelupgamer.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del producto. Aunque en la base de datos la columna sigue llamándose
     * "nombre", preferimos exponer un nombre de propiedad coherente con la app
     * móvil. Al anotar el nombre de la columna conservamos la compatibilidad
     * con esquemas existentes.
     */
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombreProducto;

    /**
     * Descripción del producto. Mantiene la columna original "descripcion".
     */
    @Column(name = "descripcion", length = 500)
    private String descripcionProducto;

    /**
     * Precio unitario del producto. Conserva la columna original "precio".
     */
    @Column(name = "precio", nullable = false)
    private Double precioProducto;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    /**
     * Cantidad disponible en stock. Se expone como "cantidadDisponible" para
     * coincidir con la entidad de productos de la app móvil. La columna
     * subyacente en la base de datos se mantiene como "stock_disponible".
     */
    @Column(name = "stock_disponible", nullable = false)
    private Integer cantidadDisponible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}

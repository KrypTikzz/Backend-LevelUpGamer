package com.levelupgamer.backend.dtos;

import lombok.Data;

@Data
public class DetallePedidoDTO {

    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}

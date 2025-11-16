package com.levelupgamer.backend.dtos;

import lombok.Data;

@Data
public class ItemPedidoRequest {
    private Long productoId;
    private Integer cantidad;
}

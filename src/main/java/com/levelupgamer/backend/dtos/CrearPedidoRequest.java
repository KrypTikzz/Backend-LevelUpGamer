package com.levelupgamer.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CrearPedidoRequest {
    private Long usuarioId;
    private List<ItemPedidoRequest> items;
}

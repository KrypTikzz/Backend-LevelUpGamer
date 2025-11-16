package com.levelupgamer.backend.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String correoUsuario;
    private LocalDateTime fechaCreacion;
    private Double total;
    private List<DetallePedidoDTO> detalles;
}

package com.levelupgamer.backend.services;

import com.levelupgamer.backend.dtos.CrearPedidoRequest;
import com.levelupgamer.backend.dtos.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

    PedidoResponseDTO crearPedido(CrearPedidoRequest request);

    PedidoResponseDTO obtenerPorId(Long id);

    List<PedidoResponseDTO> listarPorUsuario(Long usuarioId);

    List<PedidoResponseDTO> listarTodos();
}


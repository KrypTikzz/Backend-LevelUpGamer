package com.levelupgamer.backend.controllers;

import com.levelupgamer.backend.dtos.CrearPedidoRequest;
import com.levelupgamer.backend.dtos.PedidoResponseDTO;
import com.levelupgamer.backend.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar pedidos (compras).
 * Ruta base: /api/pedidos
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    // POST /api/pedidos  -> crear pedido
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(@RequestBody CrearPedidoRequest request) {
        PedidoResponseDTO creado = pedidoService.crearPedido(request);
        return ResponseEntity.ok(creado);
    }

    // GET /api/pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    // GET /api/pedidos/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    // GET /api/pedidos
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
}


package com.levelupgamer.backend.services;

import com.levelupgamer.backend.dtos.CrearPedidoRequest;
import com.levelupgamer.backend.dtos.DetallePedidoDTO;
import com.levelupgamer.backend.dtos.ItemPedidoRequest;
import com.levelupgamer.backend.dtos.PedidoResponseDTO;
import com.levelupgamer.backend.models.entities.*;
import com.levelupgamer.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public PedidoResponseDTO crearPedido(CrearPedidoRequest request) {
        if (request.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId es obligatorio");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe incluir al menos un producto en el pedido");
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con id " + request.getUsuarioId()
                ));

        // Crear pedido vacío y luego llenarlo
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaCreacion(LocalDateTime.now());

        List<DetallePedido> detalles = new ArrayList<>();
        double total = 0.0;

        for (ItemPedidoRequest item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Producto no encontrado con id " + item.getProductoId()
                    ));

            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cantidad inválida para producto con id " + item.getProductoId()
                );
            }

            // (Opcional) validar stock
            if (producto.getCantidadDisponible() != null &&
                    producto.getCantidadDisponible() < item.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No hay stock suficiente para el producto " + producto.getNombreProducto()
                );
            }

            // Actualizar stock
            if (producto.getCantidadDisponible() != null) {
                producto.setCantidadDisponible(producto.getCantidadDisponible() - item.getCantidad());
                productoRepository.save(producto);
            }

            Double precioUnitario = producto.getPrecioProducto();
            Double subtotal = precioUnitario * item.getCantidad();
            total += subtotal;

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);

            detalles.add(detalle);
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        // Guardar pedido y detalles (gracias al cascade ALL también se guardan detalles)
        Pedido guardado = pedidoRepository.save(pedido);

        return mapearAPedidoResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido no encontrado con id " + id
                ));

        return mapearAPedidoResponseDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con id " + usuarioId
                ));

        return pedidoRepository.findByUsuario(usuario)
                .stream()
                .map(this::mapearAPedidoResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::mapearAPedidoResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================
    // Métodos auxiliares
    // =====================

    private PedidoResponseDTO mapearAPedidoResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setCorreoUsuario(pedido.getUsuario().getCorreo());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setTotal(pedido.getTotal());

        List<DetallePedidoDTO> detalleDTOs = pedido.getDetalles()
                .stream()
                .map(det -> {
                    DetallePedidoDTO d = new DetallePedidoDTO();
                    d.setProductoId(det.getProducto().getId());
                    d.setNombreProducto(det.getProducto().getNombreProducto());
                    d.setCantidad(det.getCantidad());
                    d.setPrecioUnitario(det.getPrecioUnitario());
                    d.setSubtotal(det.getSubtotal());
                    return d;
                })
                .collect(Collectors.toList());

        dto.setDetalles(detalleDTOs);
        return dto;
    }
}

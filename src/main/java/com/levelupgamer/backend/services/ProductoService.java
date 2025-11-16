package com.levelupgamer.backend.services;

import com.levelupgamer.backend.dtos.ProductoDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> listarTodos();

    ProductoDTO obtenerPorId(Long id);

    List<ProductoDTO> listarPorCategoria(Long categoriaId);

    ProductoDTO crearProducto(ProductoDTO dto);

    ProductoDTO actualizarProducto(Long id, ProductoDTO dto);

    void eliminarProducto(Long id);
}


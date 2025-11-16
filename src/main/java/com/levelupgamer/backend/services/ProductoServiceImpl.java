package com.levelupgamer.backend.services;


import com.levelupgamer.backend.dtos.ProductoDTO;
import com.levelupgamer.backend.models.entities.Categoria;
import com.levelupgamer.backend.models.entities.Producto;
import com.levelupgamer.backend.repositories.CategoriaRepository;
import com.levelupgamer.backend.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapearAProductoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));
        return mapearAProductoDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorCategoria(Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + categoriaId));

        return productoRepository.findByCategoria(categoria)
                .stream()
                .map(this::mapearAProductoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO crearProducto(ProductoDTO dto) {
        Categoria categoria = obtenerOCrearCategoria(dto);

        Producto producto = new Producto();
        // Usamos los nombres de propiedades alineados con la app móvil.
        producto.setNombreProducto(dto.getNombreProducto());
        producto.setDescripcionProducto(dto.getDescripcionProducto());
        producto.setPrecioProducto(dto.getPrecioProducto());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCantidadDisponible(dto.getCantidadDisponible());
        producto.setCategoria(categoria);

        Producto guardado = productoRepository.save(producto);

        return mapearAProductoDTO(guardado);
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));

        Categoria categoria = obtenerOCrearCategoria(dto);

        producto.setNombreProducto(dto.getNombreProducto());
        producto.setDescripcionProducto(dto.getDescripcionProducto());
        producto.setPrecioProducto(dto.getPrecioProducto());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCantidadDisponible(dto.getCantidadDisponible());
        producto.setCategoria(categoria);

        Producto actualizado = productoRepository.save(producto);

        return mapearAProductoDTO(actualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id " + id);
        }
        productoRepository.deleteById(id);
    }

    // =======================
    // Métodos auxiliares
    // =======================

    private Categoria obtenerOCrearCategoria(ProductoDTO dto) {
        // Si hay id de categoría, usamos la existente
        if (dto.getCategoriaId() != null) {
            return categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + dto.getCategoriaId()));
        }

        // Si se proporcionó el nombre de la categoría (categoriaProducto), buscar o crear
        if (dto.getCategoriaProducto() != null && !dto.getCategoriaProducto().isBlank()) {
            String nombre = dto.getCategoriaProducto().trim();
            return categoriaRepository
                    .findByNombreCategoriaIgnoreCase(nombre)
                    .orElseGet(() -> {
                        Categoria nueva = new Categoria();
                        nueva.setNombreCategoria(nombre);
                        nueva.setDescripcionCategoria("Creada automáticamente");
                        return categoriaRepository.save(nueva);
                    });
        }

        // Categoría por defecto
        Categoria defaultCat = categoriaRepository
                .findByNombreCategoriaIgnoreCase("General")
                .orElseGet(() -> {
                    Categoria c = new Categoria();
                    c.setNombreCategoria("General");
                    c.setDescripcionCategoria("Categoría por defecto");
                    return categoriaRepository.save(c);
                });

        return defaultCat;
    }

    private ProductoDTO mapearAProductoDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcionProducto(producto.getDescripcionProducto());
        dto.setPrecioProducto(producto.getPrecioProducto());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setCantidadDisponible(producto.getCantidadDisponible());

        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaProducto(producto.getCategoria().getNombreCategoria());
        }

        return dto;
    }
}

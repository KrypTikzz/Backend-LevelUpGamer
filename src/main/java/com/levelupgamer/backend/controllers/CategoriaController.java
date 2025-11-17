package com.levelupgamer.backend.controllers;

import com.levelupgamer.backend.models.entities.Categoria;
import com.levelupgamer.backend.repositories.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar categorías de productos.
 *
 * Este controlador expone operaciones CRUD completas (obtener todas,
 * obtener por id, crear, actualizar y eliminar) sobre la entidad
 * {@link Categoria}. De este modo la aplicación móvil puede
 * gestionar categorías exclusivamente a través del backend sin
 * necesidad de almacenamiento local.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Devuelve la lista completa de categorías disponibles.
     */
    @GetMapping
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * Devuelve los detalles de una categoría concreta por su id.
     *
     * @param id identificador de la categoría
     * @return 200 con la categoría encontrada o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoria(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva categoría a partir del cuerpo recibido.
     *
     * @param categoria datos de la categoría
     * @return la categoría creada
     */
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        Categoria saved = categoriaRepository.save(categoria);
        return ResponseEntity.ok(saved);
    }

    /**
     * Actualiza una categoría existente. Si no existe se devuelve 404.
     *
     * @param id        identificador de la categoría a actualizar
     * @param categoria datos de la categoría con nombre y descripción
     * @return 200 con la categoría actualizada o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id,
                                                         @RequestBody Categoria categoria) {
        return categoriaRepository.findById(id)
                .map(existing -> {
                    existing.setNombreCategoria(categoria.getNombreCategoria());
                    existing.setDescripcionCategoria(categoria.getDescripcionCategoria());
                    Categoria updated = categoriaRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una categoría por id. Devuelve 204 si se elimina o 404 si no se encuentra.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        // Si no existe, devolvemos 404 directamente
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Si existe, la eliminamos y devolvemos 204 (sin contenido)
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

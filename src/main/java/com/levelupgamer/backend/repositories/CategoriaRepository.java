package com.levelupgamer.backend.repositories;

import com.levelupgamer.backend.models.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca una categoría por su nombre (ignorando mayúsculas y minúsculas).
     * Se ajusta al nombre de propiedad "nombreCategoria" de la entidad.
     */
    Optional<Categoria> findByNombreCategoriaIgnoreCase(String nombreCategoria);
}

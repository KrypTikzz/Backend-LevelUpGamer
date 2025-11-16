package com.levelupgamer.backend.repositories;

import com.levelupgamer.backend.models.entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}

package com.levelupgamer.backend.repositories;

import com.levelupgamer.backend.models.entities.Pedido;
import com.levelupgamer.backend.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuario(Usuario usuario);
}

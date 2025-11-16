package com.levelupgamer.backend.services;

import com.levelupgamer.backend.models.entities.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio para usuarios.
 * El controlador habla con esta interfaz, no directamente con el repositorio.
 */
public interface UsuarioService {

    List<Usuario> listarTodos();

    Usuario buscarPorId(Long id);

    Usuario crearUsuario(Usuario usuario);

    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);

    void eliminarUsuario(Long id);

    Optional<Usuario> buscarPorCorreo(String correo);

    Optional<Usuario> login(String correo, String contrasena);
}

package com.levelupgamer.backend.services;

import com.levelupgamer.backend.models.Rol;
import com.levelupgamer.backend.models.entities.Usuario;
import com.levelupgamer.backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "usuario no encontrado con id " + id
                ));
    }

    @Override
    @Transactional
    public Usuario crearUsuario(Usuario usuario) {

        // forzar id null para que la BD genere uno
        usuario.setId(null);

        // si no viene rol asignado, se deja CLIENTE por defecto
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "usuario no encontrado con id " + id
                ));

        // actualizar campos que sí existen en Usuario.java actual
        existente.setNombre(usuarioActualizado.getNombre());
        existente.setApellido(usuarioActualizado.getApellido());
        existente.setCorreo(usuarioActualizado.getCorreo());
        existente.setContrasena(usuarioActualizado.getContrasena());
        existente.setTelefono(usuarioActualizado.getTelefono());
        existente.setRegion(usuarioActualizado.getRegion());
        existente.setComuna(usuarioActualizado.getComuna());
        existente.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());

        // actualizar rol si viene uno distinto
        if (usuarioActualizado.getRol() != null) {
            existente.setRol(usuarioActualizado.getRol());
        }

        return usuarioRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "usuario no encontrado con id " + id
            );
        }

        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> login(String correo, String contrasena) {
        return usuarioRepository.findByCorreo(correo)
                .filter(u -> Objects.equals(u.getContrasena(), contrasena));
    }
}

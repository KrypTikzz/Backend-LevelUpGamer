package com.levelupgamer.backend.services;

import com.levelupgamer.backend.models.entities.Usuario;
import com.levelupgamer.backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
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
                        "Usuario no encontrado con id " + id
                ));
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe un usuario registrado con el correo " + usuario.getCorreo()
            );
        }

        // Aseguramos que se genere un id nuevo
        usuario.setId(null);

        // Si admin viene nulo, lo dejamos en false
        if (usuario.getAdmin() == null) {
            usuario.setAdmin(false);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con id " + id
                ));

        // Si quiere cambiar el correo, validamos que no esté usado por otro usuario
        if (!Objects.equals(existente.getCorreo(), usuarioActualizado.getCorreo()) &&
                usuarioRepository.existsByCorreo(usuarioActualizado.getCorreo())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe un usuario con el correo " + usuarioActualizado.getCorreo()
            );
        }

        existente.setNombre(usuarioActualizado.getNombre());
        existente.setApellido(usuarioActualizado.getApellido());
        existente.setCorreo(usuarioActualizado.getCorreo());
        existente.setContrasena(usuarioActualizado.getContrasena());
        existente.setEdad(usuarioActualizado.getEdad());
        existente.setAdmin(
                usuarioActualizado.getAdmin() != null ? usuarioActualizado.getAdmin() : existente.getAdmin()
        );

        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado con id " + id
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

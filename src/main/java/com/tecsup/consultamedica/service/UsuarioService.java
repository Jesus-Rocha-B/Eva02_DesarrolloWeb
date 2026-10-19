package com.tecsup.consultamedica.service;

import com.tecsup.consultamedica.model.Rol;
import com.tecsup.consultamedica.model.Usuario;
import com.tecsup.consultamedica.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario registrar(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        if (usuario.getEstado() == null || usuario.getEstado().isBlank()) {
            usuario.setEstado("ACTIVO");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datosActualizados, Set<Rol> nuevosRoles) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombres(datosActualizados.getNombres());
            usuario.setApellidos(datosActualizados.getApellidos());
            usuario.setCorreo(datosActualizados.getCorreo());

            // Solo si se ingresó una nueva contraseña se vuelve a encriptar con BCrypt
            if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
            }

            if (nuevosRoles != null) {
                usuario.setRoles(nuevosRoles);
            }
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public void cambiarEstado(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            if ("ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
                usuario.setEstado("INACTIVO");
            } else {
                usuario.setEstado("ACTIVO");
            }
            usuarioRepository.save(usuario);
        });
    }

    @Transactional(readOnly = true)
    public long contarActivos() {
        return usuarioRepository.countByEstado("ACTIVO");
    }

    @Transactional(readOnly = true)
    public long contarInactivos() {
        return usuarioRepository.countByEstado("INACTIVO");
    }
}

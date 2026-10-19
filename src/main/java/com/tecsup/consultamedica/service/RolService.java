package com.tecsup.consultamedica.service;

import com.tecsup.consultamedica.aop.Auditable;
import com.tecsup.consultamedica.model.Rol;
import com.tecsup.consultamedica.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Rol> obtenerPorId(Long id) {
        return rolRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Rol> obtenerPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Auditable(operacion = "REGISTRO", entidad = "Rol")
    public Rol guardar(Rol rol) {
        if (rol.getEstado() == null || rol.getEstado().isBlank()) {
            rol.setEstado("ACTIVO");
        }
        return rolRepository.save(rol);
    }

    @Auditable(operacion = "DESACTIVACION", entidad = "Rol")
    public void cambiarEstado(Long id) {
        rolRepository.findById(id).ifPresent(rol -> {
            if ("ACTIVO".equalsIgnoreCase(rol.getEstado())) {
                rol.setEstado("INACTIVO");
            } else {
                rol.setEstado("ACTIVO");
            }
            rolRepository.save(rol);
        });
    }
}

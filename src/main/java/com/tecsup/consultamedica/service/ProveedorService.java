package com.tecsup.consultamedica.service;

import com.tecsup.consultamedica.model.Proveedor;
import com.tecsup.consultamedica.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepo;

    public ProveedorService(ProveedorRepository proveedorRepo) {
        this.proveedorRepo = proveedorRepo;
    }

    public List<Proveedor> listarTodos() {
        return proveedorRepo.findAll();
    }

    public List<Proveedor> listarActivos() {
        return proveedorRepo.findByEstado("ACTIVO");
    }

    public Proveedor buscarPorId(Long id) {
        return proveedorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
    }

    @Transactional
    public Proveedor guardar(Proveedor proveedor) {
        if (proveedor.getId() == null && proveedorRepo.findByRuc(proveedor.getRuc()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un proveedor registrado con el RUC: " + proveedor.getRuc());
        }
        if (proveedor.getEstado() == null || proveedor.getEstado().isBlank()) {
            proveedor.setEstado("ACTIVO");
        }
        return proveedorRepo.save(proveedor);
    }

    @Transactional
    public void cambiarEstado(Long id) {
        Proveedor proveedor = buscarPorId(id);
        proveedor.setEstado("ACTIVO".equalsIgnoreCase(proveedor.getEstado()) ? "INACTIVO" : "ACTIVO");
        proveedorRepo.save(proveedor);
    }
}


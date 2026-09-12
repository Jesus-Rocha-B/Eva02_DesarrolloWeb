package com.tecsup.consultamedica.service;
import com.tecsup.consultamedica.model.PrincipioActivo;
import com.tecsup.consultamedica.repository.PrincipioActivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrincipioActivoService {

    @Autowired
    private PrincipioActivoRepository principioActivoRepository;

    public List<PrincipioActivo> listarTodos() {
        return principioActivoRepository.findAll();
    }

    public PrincipioActivo buscarPorId(Long id) {
        return principioActivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Principio activo no encontrado con id " + id));
    }

    public PrincipioActivo registrar(PrincipioActivo principioActivo) {
        principioActivo.setEstado("ACTIVO");
        return principioActivoRepository.save(principioActivo);
    }

    public PrincipioActivo actualizar(Long id, PrincipioActivo datos) {
        PrincipioActivo existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return principioActivoRepository.save(existente);
    }

    public void eliminar(Long id) {
        principioActivoRepository.deleteById(id);
    }
}
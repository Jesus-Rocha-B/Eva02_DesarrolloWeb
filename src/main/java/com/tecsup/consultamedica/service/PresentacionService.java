package com.tecsup.consultamedica.service;
import com.tecsup.consultamedica.model.Presentacion;
import com.tecsup.consultamedica.repository.PresentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PresentacionService {
    @Autowired
    private PresentacionRepository presentacionRepository;

    public List<Presentacion> listarTodas() {
        return presentacionRepository.findAll();
    }

    public Presentacion buscarPorId(Long id) {
        return presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentación no encontrada con id " + id));
    }

    public Presentacion registrar(Presentacion presentacion) {
        presentacion.setEstado("ACTIVO");
        return presentacionRepository.save(presentacion);
    }

    public Presentacion actualizar(Long id, Presentacion datos) {
        Presentacion existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return presentacionRepository.save(existente);
    }

    public void eliminar(Long id) {
        presentacionRepository.deleteById(id);
    }
}
package com.tecsup.consultamedica.service;
import com.tecsup.consultamedica.model.Laboratorio;
import com.tecsup.consultamedica.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public List<Laboratorio> listarTodos() {
        return laboratorioRepository.findAll();
    }

    public Laboratorio buscarPorId(Long id) {
        return laboratorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado con id " + id));
    }

    public Laboratorio registrar(Laboratorio laboratorio) {
        laboratorio.setEstado("ACTIVO");
        return laboratorioRepository.save(laboratorio);
    }

    public Laboratorio actualizar(Long id, Laboratorio datos) {
        Laboratorio existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDireccion(datos.getDireccion());
        existente.setTelefono(datos.getTelefono());
        existente.setCorreo(datos.getCorreo());
        return laboratorioRepository.save(existente);
    }

    public void eliminar(Long id) {
        laboratorioRepository.deleteById(id);
    }
}
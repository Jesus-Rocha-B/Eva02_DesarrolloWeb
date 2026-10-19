package com.tecsup.consultamedica.service;
import com.tecsup.consultamedica.aop.Auditable;
import com.tecsup.consultamedica.model.Medicamento;
import com.tecsup.consultamedica.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public List<Medicamento> listarTodos() {
        return medicamentoRepository.findAll();
    }

    public Medicamento buscarPorId(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con id " + id));
    }

    // RF FAR 03 y 06
    @Auditable(operacion = "REGISTRO", entidad = "Medicamento")
    public Medicamento registrar(Medicamento medicamento) {
        medicamentoRepository.findByCodigo(medicamento.getCodigo()).ifPresent(m -> {
            throw new RuntimeException("Ya existe un medicamento con el código " + medicamento.getCodigo());
        });
        medicamento.setEstado("ACTIVO");
        return medicamentoRepository.save(medicamento);
    }

    // RF FAR 04: modificar
    @Auditable(operacion = "MODIFICACION", entidad = "Medicamento")
    public Medicamento actualizar(Long id, Medicamento datos) {
        Medicamento existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setPrincipioActivo(datos.getPrincipioActivo());
        existente.setCategoria(datos.getCategoria());
        existente.setPresentacion(datos.getPresentacion());
        existente.setLaboratorio(datos.getLaboratorio());
        existente.setDescripcion(datos.getDescripcion());
        existente.setRequiereReceta(datos.isRequiereReceta());
        return medicamentoRepository.save(existente);
    }

    // RF FAR 05: activar o desactivar
    @Auditable(operacion = "DESACTIVACION", entidad = "Medicamento")
    public Medicamento cambiarEstado(Long id, String nuevoEstado) {
        Medicamento existente = buscarPorId(id);
        existente.setEstado(nuevoEstado);
        return medicamentoRepository.save(existente);
    }
}
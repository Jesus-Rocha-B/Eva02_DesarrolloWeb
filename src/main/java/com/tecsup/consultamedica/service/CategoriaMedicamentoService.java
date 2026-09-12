package com.tecsup.consultamedica.service;
import com.tecsup.consultamedica.model.CategoriaMedicamento;
import com.tecsup.consultamedica.repository.CategoriaMedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaMedicamentoService {
    @Autowired
    private CategoriaMedicamentoRepository categoriaRepository;

    public List<CategoriaMedicamento> listarTodas() {
        return categoriaRepository.findAll();
    }

    public CategoriaMedicamento buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + id));
    }

    public CategoriaMedicamento registrar(CategoriaMedicamento categoria) {
        categoria.setEstado("ACTIVO");
        return categoriaRepository.save(categoria);
    }

    public CategoriaMedicamento actualizar(Long id, CategoriaMedicamento datos) {
        CategoriaMedicamento existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return categoriaRepository.save(existente);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
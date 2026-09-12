package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.model.CategoriaMedicamento;
import com.tecsup.consultamedica.service.CategoriaMedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaMedicamentoController {

    @Autowired
    private CategoriaMedicamentoService categoriaService;

    @GetMapping
    public List<CategoriaMedicamento> listar() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public CategoriaMedicamento buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    public CategoriaMedicamento registrar(@RequestBody CategoriaMedicamento categoria) {
        return categoriaService.registrar(categoria);
    }

    @PutMapping("/{id}")
    public CategoriaMedicamento actualizar(@PathVariable Long id, @RequestBody CategoriaMedicamento categoria) {
        return categoriaService.actualizar(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }
}
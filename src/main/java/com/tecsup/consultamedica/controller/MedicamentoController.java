package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.model.Medicamento;
import com.tecsup.consultamedica.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    public List<Medicamento> listar() {
        return medicamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Medicamento buscarPorId(@PathVariable Long id) {
        return medicamentoService.buscarPorId(id);
    }

    @PostMapping
    public Medicamento registrar(@RequestBody Medicamento medicamento) {
        return medicamentoService.registrar(medicamento);
    }

    @PutMapping("/{id}")
    public Medicamento actualizar(@PathVariable Long id, @RequestBody Medicamento medicamento) {
        return medicamentoService.actualizar(id, medicamento);
    }

    @PatchMapping("/{id}/estado")
    public Medicamento cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return medicamentoService.cambiarEstado(id, body.get("estado"));
    }
}
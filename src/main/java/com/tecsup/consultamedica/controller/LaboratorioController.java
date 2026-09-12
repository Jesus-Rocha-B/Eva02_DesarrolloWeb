package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.model.Laboratorio;
import com.tecsup.consultamedica.service.LaboratorioService;
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
@RequestMapping("/api/laboratorios")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping
    public List<Laboratorio> listar() {
        return laboratorioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Laboratorio buscarPorId(@PathVariable Long id) {
        return laboratorioService.buscarPorId(id);
    }

    @PostMapping
    public Laboratorio registrar(@RequestBody Laboratorio laboratorio) {
        return laboratorioService.registrar(laboratorio);
    }

    @PutMapping("/{id}")
    public Laboratorio actualizar(@PathVariable Long id, @RequestBody Laboratorio laboratorio) {
        return laboratorioService.actualizar(id, laboratorio);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        laboratorioService.eliminar(id);
    }
}
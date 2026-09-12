package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.model.PrincipioActivo;
import com.tecsup.consultamedica.service.PrincipioActivoService;
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
@RequestMapping("/api/principios-activos")
public class PrincipioActivoController {

    @Autowired
    private PrincipioActivoService principioActivoService;

    @GetMapping
    public List<PrincipioActivo> listar() {
        return principioActivoService.listarTodos();
    }

    @GetMapping("/{id}")
    public PrincipioActivo buscarPorId(@PathVariable Long id) {
        return principioActivoService.buscarPorId(id);
    }

    @PostMapping
    public PrincipioActivo registrar(@RequestBody PrincipioActivo principioActivo) {
        return principioActivoService.registrar(principioActivo);
    }

    @PutMapping("/{id}")
    public PrincipioActivo actualizar(@PathVariable Long id, @RequestBody PrincipioActivo principioActivo) {
        return principioActivoService.actualizar(id, principioActivo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        principioActivoService.eliminar(id);
    }
}
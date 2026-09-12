package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.model.Presentacion;
import com.tecsup.consultamedica.service.PresentacionService;
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
@RequestMapping("/api/presentaciones")
public class PresentacionController {

    @Autowired
    private PresentacionService presentacionService;

    @GetMapping
    public List<Presentacion> listar() {
        return presentacionService.listarTodas();
    }

    @GetMapping("/{id}")
    public Presentacion buscarPorId(@PathVariable Long id) {
        return presentacionService.buscarPorId(id);
    }

    @PostMapping
    public Presentacion registrar(@RequestBody Presentacion presentacion) {
        return presentacionService.registrar(presentacion);
    }

    @PutMapping("/{id}")
    public Presentacion actualizar(@PathVariable Long id, @RequestBody Presentacion presentacion) {
        return presentacionService.actualizar(id, presentacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        presentacionService.eliminar(id);
    }
}
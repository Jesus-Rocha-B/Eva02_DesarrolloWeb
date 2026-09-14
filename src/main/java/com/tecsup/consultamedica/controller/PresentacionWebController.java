package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.model.Presentacion;
import com.tecsup.consultamedica.service.PresentacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/presentaciones")
public class PresentacionWebController {

    private final PresentacionService presentacionService;

    public PresentacionWebController(PresentacionService presentacionService) {
        this.presentacionService = presentacionService;
    }

    @GetMapping
    public String verLista(Model model) {
        model.addAttribute("presentaciones", presentacionService.listarTodas());
        model.addAttribute("nuevaPresentacion", new Presentacion());
        return "presentaciones";
    }

    @PostMapping
    public String registrar(@ModelAttribute("nuevaPresentacion") Presentacion presentacion) {
        presentacion.setEstado("ACTIVO");
        presentacionService.registrar(presentacion);
        return "redirect:/web/presentaciones";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String alternarEstado(@PathVariable("id") Long id) {
        Presentacion pres = presentacionService.buscarPorId(id);
        String nuevoEstado = "ACTIVO".equalsIgnoreCase(pres.getEstado()) ? "INACTIVO" : "ACTIVO";
        presentacionService.cambiarEstado(id, nuevoEstado);
        return "redirect:/web/presentaciones";
    }
}
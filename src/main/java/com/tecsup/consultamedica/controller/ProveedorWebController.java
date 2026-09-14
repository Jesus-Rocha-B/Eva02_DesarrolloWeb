package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.model.Proveedor;
import com.tecsup.consultamedica.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/proveedores")
public class ProveedorWebController {

    private final ProveedorService proveedorService;

    public ProveedorWebController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", proveedorService.listarTodos());
        model.addAttribute("nuevoProveedor", new Proveedor());
        return "proveedores";
    }

    @PostMapping
    public String registrar(@ModelAttribute("nuevoProveedor") Proveedor proveedor, RedirectAttributes flash) {
        try {
            proveedorService.guardar(proveedor);
            flash.addFlashAttribute("mensajeExito", "Proveedor registrado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/web/proveedores";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes flash) {
        try {
            proveedorService.cambiarEstado(id);
            flash.addFlashAttribute("mensajeExito", "Estado del proveedor actualizado exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/web/proveedores";
    }
}
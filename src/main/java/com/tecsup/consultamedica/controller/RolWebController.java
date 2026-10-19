package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.model.Rol;
import com.tecsup.consultamedica.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/roles")
public class RolWebController {

    private final RolService rolService;

    public RolWebController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public String listarRoles(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("nuevoRol", new Rol());
        return "roles";
    }

    @PostMapping("/guardar")
    public String guardarRol(@ModelAttribute Rol rol, RedirectAttributes redirectAttributes) {
        try {
            if (rol.getNombre() != null && !rol.getNombre().startsWith("ROLE_")) {
                rol.setNombre("ROLE_" + rol.getNombre().toUpperCase().trim());
            }
            rolService.guardar(rol);
            redirectAttributes.addFlashAttribute("mensajeExito", "Rol '" + rol.getNombre() + "' registrado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar rol: " + e.getMessage());
        }
        return "redirect:/web/roles";
    }

    @PostMapping("/editar")
    public String editarRol(@ModelAttribute Rol rol, RedirectAttributes redirectAttributes) {
        try {
            if (rol.getNombre() != null && !rol.getNombre().startsWith("ROLE_")) {
                rol.setNombre("ROLE_" + rol.getNombre().toUpperCase().trim());
            }
            rolService.guardar(rol);
            redirectAttributes.addFlashAttribute("mensajeExito", "Rol actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar rol: " + e.getMessage());
        }
        return "redirect:/web/roles";
    }

    @PostMapping("/toggle/{id}")
    public String cambiarEstadoRol(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.cambiarEstado(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Estado del rol modificado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/web/roles";
    }
}

package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.model.Rol;
import com.tecsup.consultamedica.model.Usuario;
import com.tecsup.consultamedica.service.RolService;
import com.tecsup.consultamedica.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/web/usuarios")
public class UsuarioWebController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioWebController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("nuevoUsuario", new Usuario());
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("activos", usuarioService.contarActivos());
        model.addAttribute("inactivos", usuarioService.contarInactivos());
        return "usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
                                 @RequestParam(value = "rolId", required = false) Long rolId,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (rolId != null) {
                rolService.obtenerPorId(rolId).ifPresent(rol -> usuario.setRoles(Set.of(rol)));
            }
            usuarioService.registrar(usuario);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario '" + usuario.getUsername() + "' registrado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al registrar usuario: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }

    @PostMapping("/editar")
    public String editarUsuario(@RequestParam("id") Long id,
                                @ModelAttribute Usuario usuarioActualizado,
                                @RequestParam(value = "rolId", required = false) Long rolId,
                                RedirectAttributes redirectAttributes) {
        try {
            Set<Rol> roles = new HashSet<>();
            if (rolId != null) {
                rolService.obtenerPorId(rolId).ifPresent(roles::add);
            }
            usuarioService.actualizar(id, usuarioActualizado, roles);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario '" + usuarioActualizado.getUsername() + "' actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar usuario: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }

    @PostMapping("/toggle/{id}")
    public String cambiarEstadoUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarEstado(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Estado del usuario actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }
}

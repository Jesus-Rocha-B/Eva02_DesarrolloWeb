package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.model.RecetaMedica;
import com.tecsup.consultamedica.service.MedicamentoService;
import com.tecsup.consultamedica.service.RecetaMedicaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/recetas")
public class RecetaWebController {

    private final RecetaMedicaService recetaService;
    private final MedicamentoService medicamentoService;

    public RecetaWebController(RecetaMedicaService recetaService, MedicamentoService medicamentoService) {
        this.recetaService = recetaService;
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public String listarBandeja(Model model) {
        model.addAttribute("recetas", recetaService.listarTodas());
        return "recetas";
    }

    @GetMapping("/nueva")
    public String formularioNuevaReceta(Model model) {
        model.addAttribute("medicamentos", medicamentoService.listarTodos());
        return "receta-form";
    }

    @PostMapping("/guardar")
    public String guardarReceta(@RequestParam String dniPaciente,
                                @RequestParam String nombrePaciente,
                                @RequestParam String historiaClinica,
                                @RequestParam String medicoNombre,
                                @RequestParam(required = false) String medicoColegiatura,
                                @RequestParam(required = false) String diagnostico,
                                @RequestParam Long medicamentoId,
                                @RequestParam String dosis,
                                @RequestParam String frecuencia,
                                @RequestParam String duracion,
                                @RequestParam String via,
                                @RequestParam Integer cantidad,
                                @RequestParam(required = false) String indicaciones,
                                RedirectAttributes redirectAttributes) {
        try {
            RecetaMedica guardada = recetaService.emitirReceta(
                    dniPaciente, nombrePaciente, historiaClinica,
                    medicoNombre, medicoColegiatura, diagnostico,
                    medicamentoId, dosis, frecuencia, duracion, via, cantidad, indicaciones
            );
            redirectAttributes.addFlashAttribute("mensaje", "Receta " + guardada.getNumeroReceta() + " emitida correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al emitir receta: " + e.getMessage());
            return "redirect:/web/recetas/nueva";
        }
        return "redirect:/web/recetas";
    }

    @PostMapping("/{id}/dispensar")
    public String dispensarReceta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            recetaService.dispensarReceta(id);
            redirectAttributes.addFlashAttribute("mensaje", "Receta atendida con éxito. Despacho FEFO registrado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Fallo de dispensación: " + e.getMessage());
        }
        return "redirect:/web/recetas";
    }
}
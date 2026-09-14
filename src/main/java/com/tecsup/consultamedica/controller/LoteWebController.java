package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.model.Lote;
import com.tecsup.consultamedica.model.Medicamento; // <-- [NUEVO] Modelo Medicamento
import com.tecsup.consultamedica.service.LoteService;
import com.tecsup.consultamedica.service.MedicamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List; // <-- [NUEVO] Lista genérica

@Controller
@RequestMapping("/web/lotes")
public class LoteWebController {

    private final LoteService loteService;
    private final MedicamentoService medicamentoService;

    public LoteWebController(LoteService loteService, MedicamentoService medicamentoService) {
        this.loteService = loteService;
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public String verLista(Model model) {
        // <-- [CAMBIO] Obtenemos la lista para evaluar existencias
        List<Medicamento> medicamentos = medicamentoService.listarTodos();

        // <-- [CAMBIO] Cálculo de alertas preventivas (RF-FAR-04)
        List<Lote> lotesPorVencer = loteService.listarProximosAVencer(30);
        List<Medicamento> medicamentosSinStock = medicamentos.stream()
                .filter(m -> loteService.obtenerStockTotal(m.getId()) == 0)
                .toList();

        model.addAttribute("lotes", loteService.listarTodos());
        model.addAttribute("medicamentos", medicamentos);
        model.addAttribute("nuevoLote", new Lote());
        model.addAttribute("lotesPorVencer", lotesPorVencer);         // <-- [CAMBIO] Enviado a la vista
        model.addAttribute("medicamentosSinStock", medicamentosSinStock); // <-- [CAMBIO] Enviado a la vista
        return "lotes";
    }

    @PostMapping
    public String registrar(@ModelAttribute("nuevoLote") Lote lote) {
        loteService.registrarIngreso(lote);
        return "redirect:/web/lotes";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String alternarEstado(@PathVariable("id") Long id) {
        Lote lote = loteService.buscarPorId(id);
        String nuevoEstado = "DISPONIBLE".equalsIgnoreCase(lote.getEstado()) ? "AGOTADO" : "DISPONIBLE";
        loteService.cambiarEstado(id, nuevoEstado);
        return "redirect:/web/lotes";
    }

    @PostMapping("/dispensar")
    public String dispensarMedicamento(
            @RequestParam("medicamentoId") Long medicamentoId,
            @RequestParam("cantidad") Integer cantidad,
            RedirectAttributes redirectAttributes) {
        try {
            loteService.dispensarMedicamento(medicamentoId, cantidad);
            redirectAttributes.addFlashAttribute("mensajeExito", "Dispensación procesada con éxito aplicando criterio FEFO.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/web/lotes";
    }

    // <-- [NUEVO] Endpoint unificado para ajustar existencias (Entrada / Merma)
    @PostMapping("/{id}/ajustar")
    public String ajustarStock(
            @PathVariable("id") Long id,
            @RequestParam("tipo") String tipo,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam(value = "motivo", required = false) String motivo,
            RedirectAttributes redirectAttributes) {
        try {
            loteService.ajustarStock(id, tipo, cantidad, motivo);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ajuste de inventario procesado correctamente (" + tipo + ").");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/web/lotes";
    }
}
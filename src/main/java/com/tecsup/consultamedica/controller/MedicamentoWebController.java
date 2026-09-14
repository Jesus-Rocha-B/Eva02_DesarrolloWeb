package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.model.Medicamento;
import com.tecsup.consultamedica.service.CategoriaMedicamentoService;
import com.tecsup.consultamedica.service.LaboratorioService;
import com.tecsup.consultamedica.service.LoteService; // <-- [NUEVO] Importación del servicio de lotes
import com.tecsup.consultamedica.service.MedicamentoService;
import com.tecsup.consultamedica.service.PresentacionService;
import com.tecsup.consultamedica.service.PrincipioActivoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap; // <-- [NUEVO] Para almacenar la relación ID -> Stock
import java.util.List;    // <-- [NUEVO]
import java.util.Map;     // <-- [NUEVO]

@Controller
@RequestMapping("/web/medicamentos")
public class MedicamentoWebController {

    private final MedicamentoService medicamentoService;
    private final CategoriaMedicamentoService categoriaService;
    private final LaboratorioService laboratorioService;
    private final PresentacionService presentacionService;
    private final PrincipioActivoService principioActivoService;
    private final LoteService loteService; // <-- [NUEVO] Inyección de dependencia

    public MedicamentoWebController(
            MedicamentoService medicamentoService,
            CategoriaMedicamentoService categoriaService,
            LaboratorioService laboratorioService,
            PresentacionService presentacionService,
            PrincipioActivoService principioActivoService,
            LoteService loteService) { // <-- [NUEVO] Parámetro en el constructor
        this.medicamentoService = medicamentoService;
        this.categoriaService = categoriaService;
        this.laboratorioService = laboratorioService;
        this.presentacionService = presentacionService;
        this.principioActivoService = principioActivoService;
        this.loteService = loteService; // <-- [NUEVO] Asignación
    }

    @GetMapping
    public String listarMedicamentos(Model model) {
        // <-- [MODIFICADO] Obtenemos la lista y calculamos el stock consolidado
        List<Medicamento> medicamentos = medicamentoService.listarTodos();

        Map<Long, Integer> stockMap = new HashMap<>();
        for (Medicamento med : medicamentos) {
            stockMap.put(med.getId(), loteService.obtenerStockTotal(med.getId()));
        }

        model.addAttribute("medicamentos", medicamentos);
        model.addAttribute("stockMap", stockMap); // <-- [NUEVO] Enviado al modelo HTML
        model.addAttribute("nuevoMedicamento", new Medicamento());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("laboratorios", laboratorioService.listarTodos());
        model.addAttribute("presentaciones", presentacionService.listarTodas());
        model.addAttribute("principiosActivos", principioActivoService.listarTodos());
        return "medicamentos";
    }

    @PostMapping("/guardar")
    public String guardarMedicamento(@ModelAttribute("nuevoMedicamento") Medicamento medicamento) {
        medicamento.setEstado("ACTIVO");
        medicamentoService.registrar(medicamento);
        return "redirect:/web/medicamentos";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String alternarEstado(@PathVariable("id") Long id) {
        Medicamento med = medicamentoService.buscarPorId(id);
        String nuevoEstado = "ACTIVO".equalsIgnoreCase(med.getEstado()) ? "INACTIVO" : "ACTIVO";
        medicamentoService.cambiarEstado(id, nuevoEstado);
        return "redirect:/web/medicamentos";
    }
}
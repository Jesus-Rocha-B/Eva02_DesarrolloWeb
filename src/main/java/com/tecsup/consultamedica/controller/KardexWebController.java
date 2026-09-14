package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.model.MovimientoInventario;
import com.tecsup.consultamedica.repository.MovimientoInventarioRepository;
import com.tecsup.consultamedica.service.MedicamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/web/kardex")
public class KardexWebController {

    private final MovimientoInventarioRepository movimientoRepo;
    private final MedicamentoService medicamentoService;

    public KardexWebController(MovimientoInventarioRepository movimientoRepo, MedicamentoService medicamentoService) {
        this.movimientoRepo = movimientoRepo;
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public String verKardex(
            @RequestParam(value = "medicamentoId", required = false) Long medicamentoId,
            Model model) {

        List<MovimientoInventario> movimientos;
        if (medicamentoId != null) {
            movimientos = movimientoRepo.findByMedicamentoIdOrderByFechaHoraDesc(medicamentoId);
        } else {
            movimientos = movimientoRepo.findAllByOrderByFechaHoraDesc();
        }

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("medicamentos", medicamentoService.listarTodos());
        model.addAttribute("medicamentoSeleccionado", medicamentoId);
        return "kardex";
    }
}
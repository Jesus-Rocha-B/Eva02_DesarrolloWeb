package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.service.AuditoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/auditoria")
public class AuditoriaWebController {

    private final AuditoriaService auditoriaService;

    public AuditoriaWebController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public String verBitacora(Model model) {
        model.addAttribute("auditorias", auditoriaService.listarTodas());
        model.addAttribute("totalEventos", auditoriaService.contarTotal());
        model.addAttribute("totalRegistros", auditoriaService.contarPorOperacion("REGISTRO"));
        model.addAttribute("totalModificaciones", auditoriaService.contarPorOperacion("MODIFICACION"));
        model.addAttribute("totalBajas", auditoriaService.contarPorOperacion("DESACTIVACION"));
        return "auditoria";
    }
}

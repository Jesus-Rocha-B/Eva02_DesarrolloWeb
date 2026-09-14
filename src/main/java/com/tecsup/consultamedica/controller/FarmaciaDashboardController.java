package com.tecsup.consultamedica.controller;

import com.tecsup.consultamedica.dto.FarmaciaDashboardDTO;
import com.tecsup.consultamedica.service.FarmaciaDashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/farmacia")
public class FarmaciaDashboardController {

    private final FarmaciaDashboardService dashboardService;

    public FarmaciaDashboardController(FarmaciaDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String index() {
        return "redirect:/web/farmacia/dashboard";
    }

    @GetMapping("/dashboard")
    public String verDashboard(Model model) {
        FarmaciaDashboardDTO metricas = dashboardService.obtenerMetricas();
        model.addAttribute("m", metricas);
        return "dashboard-farmacia";
    }
}
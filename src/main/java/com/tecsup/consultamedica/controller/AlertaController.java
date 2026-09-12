package com.tecsup.consultamedica.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {
    @GetMapping("/farmacia")
    public Map<String, Object> obtenerAlertas() {
        Map<String, Object> alertas = new HashMap<>();
        alertas.put("stockBajo", List.of());
        alertas.put("proximosAVencer", List.of());
        alertas.put("mensaje", "Alertas de stock y vencimiento pendientes de implementar, requieren el modulo de lotes");
        return alertas;
    }
}
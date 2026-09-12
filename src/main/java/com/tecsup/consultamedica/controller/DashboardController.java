package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.repository.CategoriaMedicamentoRepository;
import com.tecsup.consultamedica.repository.LaboratorioRepository;
import com.tecsup.consultamedica.repository.MedicamentoRepository;
import com.tecsup.consultamedica.repository.PresentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private CategoriaMedicamentoRepository categoriaRepository;

    @Autowired
    private PresentacionRepository presentacionRepository;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @GetMapping("/farmacia")
    public Map<String, Object> obtenerDashboard() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("medicamentosRegistrados", medicamentoRepository.count());
        datos.put("medicamentosActivos", medicamentoRepository.countByEstado("ACTIVO"));
        datos.put("categoriasRegistradas", categoriaRepository.count());
        datos.put("presentacionesRegistradas", presentacionRepository.count());
        datos.put("laboratoriosRegistrados", laboratorioRepository.count());
        return datos;
    }
}
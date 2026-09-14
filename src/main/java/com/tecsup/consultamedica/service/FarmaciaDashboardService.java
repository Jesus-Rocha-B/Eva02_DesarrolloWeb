package com.tecsup.consultamedica.service;

import com.tecsup.consultamedica.dto.FarmaciaDashboardDTO;
import com.tecsup.consultamedica.model.Lote;
import com.tecsup.consultamedica.model.Medicamento;
import com.tecsup.consultamedica.repository.LoteRepository;
import com.tecsup.consultamedica.repository.MedicamentoRepository;
import com.tecsup.consultamedica.repository.MovimientoInventarioRepository;
import com.tecsup.consultamedica.repository.RecetaMedicaRepository; // <-- [NUEVO] Import
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FarmaciaDashboardService {

    private final MedicamentoRepository medicamentoRepo;
    private final LoteRepository loteRepo;
    private final MovimientoInventarioRepository movimientoRepo;
    private final LoteService loteService;
    private final RecetaMedicaRepository recetaRepo; // <-- [NUEVO] Atributo inyectado

    public FarmaciaDashboardService(MedicamentoRepository medicamentoRepo,
                                    LoteRepository loteRepo,
                                    MovimientoInventarioRepository movimientoRepo,
                                    LoteService loteService,
                                    RecetaMedicaRepository recetaRepo) { // <-- [NUEVO] Parámetro en constructor
        this.medicamentoRepo = medicamentoRepo;
        this.loteRepo = loteRepo;
        this.movimientoRepo = movimientoRepo;
        this.loteService = loteService;
        this.recetaRepo = recetaRepo;
    }

    public FarmaciaDashboardDTO obtenerMetricas() {
        FarmaciaDashboardDTO dto = new FarmaciaDashboardDTO();
        LocalDate hoy = LocalDate.now();

        // 1. Total de medicamentos registrados
        dto.setTotalMedicamentos(medicamentoRepo.count());

        // 2. Lotes próximos a vencer (ventana preventiva <= 30 días)
        List<Lote> proximosVencer = loteService.listarProximosAVencer(30);
        dto.setLotesProximosVencer(proximosVencer.size());
        dto.setListaProximosAVencer(proximosVencer);

        // 3. Consolidación de stock y detección de stock crítico (<= 10 unidades o 0)
        List<Medicamento> medicamentos = medicamentoRepo.findAll();
        int stockGlobal = 0;
        int conteoStockCritico = 0;
        List<Map<String, Object>> criticos = new ArrayList<>();

        for (Medicamento m : medicamentos) {
            Integer stockVigente = loteRepo.obtenerStockVigentePorMedicamento(m.getId(), hoy);
            int stock = (stockVigente != null) ? stockVigente : 0;
            stockGlobal += stock;

            if (stock <= 10) {
                conteoStockCritico++;
                Map<String, Object> item = new HashMap<>();
                item.put("codigo", m.getCodigo());
                item.put("nombre", m.getNombre());
                item.put("presentacion", m.getPresentacion() != null ? m.getPresentacion().getNombre() : "-");
                item.put("stock", stock);
                item.put("estadoStock", stock == 0 ? "SIN STOCK" : "BAJO");
                criticos.add(item);
            }
        }

        dto.setStockTotalDisponible(stockGlobal);
        dto.setMedicamentosStockBajo(conteoStockCritico);
        dto.setListaStockCritico(criticos);

        // 4. [MODIFICADO]: Cuenta recetas en estado PENDIENTE en tiempo real
        dto.setRecetasPendientes((int) recetaRepo.countByEstado("PENDIENTE"));

        // 5. Dispensaciones del día (Movimientos de SALIDA registrados hoy)
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.plusDays(1).atStartOfDay();
        long dispensacionesHoy = movimientoRepo.countByTipoMovimientoAndFechaHoraBetween("SALIDA", inicioDia, finDia);
        dto.setDispensacionesDelDia(dispensacionesHoy);

        return dto;
    }
}
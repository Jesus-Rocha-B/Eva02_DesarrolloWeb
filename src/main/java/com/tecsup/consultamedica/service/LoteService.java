package com.tecsup.consultamedica.service;

import com.tecsup.consultamedica.model.Lote;
import com.tecsup.consultamedica.model.MovimientoInventario;
import com.tecsup.consultamedica.repository.LoteRepository;
import com.tecsup.consultamedica.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoteService {

    private final LoteRepository loteRepository;
    private final MovimientoInventarioRepository movimientoRepo;

    public LoteService(LoteRepository loteRepository, MovimientoInventarioRepository movimientoRepo) {
        this.loteRepository = loteRepository;
        this.movimientoRepo = movimientoRepo;
    }

    public List<Lote> listarTodos() {
        return loteRepository.findAll();
    }

    public List<Lote> listarDisponibles() {
        return loteRepository.findByEstado("DISPONIBLE");
    }

    public Lote buscarPorId(Long id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado con ID: " + id));
    }

    public Integer obtenerStockTotal(Long medicamentoId) {
        Integer stock = loteRepository.obtenerStockTotalPorMedicamento(medicamentoId);
        return stock != null ? stock : 0;
    }

    public List<Lote> listarPorMedicamento(Long medicamentoId) {
        return loteRepository.findByMedicamentoId(medicamentoId);
    }

    // <-- [CORRECCIÓN RF-FAR-13]: Acotar rango entre HOY y fecha límite (excluye lotes ya vencidos)
    public List<Lote> listarProximosAVencer(int diasMargen) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(diasMargen);
        return loteRepository.findByFechaVencimientoBetweenAndEstadoOrderByFechaVencimientoAsc(hoy, fechaLimite, "DISPONIBLE");
    }

    @Transactional
    public Lote registrarIngreso(Lote lote) {
        if (lote.getCantidadActual() == null) {
            lote.setCantidadActual(lote.getCantidadInicial());
        }
        if (lote.getEstado() == null || lote.getEstado().isBlank()) {
            lote.setEstado("DISPONIBLE");
        }
        Lote guardado = loteRepository.save(lote);

        MovimientoInventario mov = new MovimientoInventario(
                guardado.getMedicamento(),
                guardado,
                "ENTRADA",
                guardado.getCantidadActual(),
                0,
                guardado.getCantidadActual(),
                "INGRESO_INICIAL"
        );
        movimientoRepo.save(mov);

        return guardado;
    }

    @Transactional
    public Lote cambiarEstado(Long id, String nuevoEstado) {
        Lote lote = buscarPorId(id);
        lote.setEstado(nuevoEstado);
        return loteRepository.save(lote);
    }

    @Transactional
    public void dispensarMedicamento(Long medicamentoId, Integer cantidadRequerida) {
        if (cantidadRequerida == null || cantidadRequerida <= 0) {
            throw new IllegalArgumentException("La cantidad a dispensar debe ser mayor a 0.");
        }

        LocalDate hoy = LocalDate.now();

        // 1. [RF-FAR-14] Validar stock considerando exclusivamente lotes vigentes
        Integer stockVigente = loteRepository.obtenerStockVigentePorMedicamento(medicamentoId, hoy);
        if (stockVigente == null || stockVigente < cantidadRequerida) {
            Integer stockTotalFisico = obtenerStockTotal(medicamentoId);
            if (stockTotalFisico >= cantidadRequerida) {
                throw new IllegalStateException("Dispensación rechazada: El stock existente en almacén se encuentra VENCIDO.");
            }
            throw new IllegalStateException("Stock insuficiente. Stock vigente apto: " + (stockVigente != null ? stockVigente : 0) + ", solicitado: " + cantidadRequerida);
        }

        // 2. [RF-FAR-14] Obtener únicamente lotes vigentes ordenados por vencimiento ascendente (FEFO)
        List<Lote> lotesAptos = loteRepository
                .findByMedicamentoIdAndEstadoAndFechaVencimientoGreaterThanEqualOrderByFechaVencimientoAsc(
                        medicamentoId, "DISPONIBLE", hoy);

        int pendientePorDespachar = cantidadRequerida;

        for (Lote lote : lotesAptos) {
            if (pendientePorDespachar <= 0) {
                break;
            }

            int disponibleEnLote = lote.getCantidadActual();
            int aDescontar = Math.min(disponibleEnLote, pendientePorDespachar);
            int saldoAnterior = disponibleEnLote;
            int saldoPosterior = disponibleEnLote - aDescontar;

            lote.setCantidadActual(saldoPosterior);
            if (saldoPosterior == 0) {
                lote.setEstado("AGOTADO");
            }
            loteRepository.save(lote);

            // Registro inmutable en Kardex
            MovimientoInventario mov = new MovimientoInventario(
                    lote.getMedicamento(),
                    lote,
                    "SALIDA",
                    aDescontar,
                    saldoAnterior,
                    saldoPosterior,
                    "DISPENSACION_FEFO"
            );
            movimientoRepo.save(mov);

            pendientePorDespachar -= aDescontar;
        }
    }

    @Transactional
    public void ajustarStock(Long loteId, String tipoAjuste, Integer cantidad, String motivo) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        Lote lote = buscarPorId(loteId);
        int saldoAnterior = lote.getCantidadActual();
        int saldoPosterior;

        if ("ENTRADA".equalsIgnoreCase(tipoAjuste)) {
            if ((saldoAnterior + cantidad) > lote.getCantidadInicial()) {
                throw new IllegalStateException("Inconsistencia: El stock resultante ("
                        + (saldoAnterior + cantidad)
                        + ") superaría el ingreso inicial registrado ("
                        + lote.getCantidadInicial()
                        + "). Si ingresó nueva mercadería, registre un nuevo lote.");
            }
            saldoPosterior = saldoAnterior + cantidad;
            lote.setCantidadActual(saldoPosterior);

            if ("AGOTADO".equalsIgnoreCase(lote.getEstado()) || saldoPosterior > 0) {
                lote.setEstado("DISPONIBLE");
            }
        } else if ("SALIDA".equalsIgnoreCase(tipoAjuste)) {
            if (saldoAnterior < cantidad) {
                throw new IllegalStateException("Saldo insuficiente en el lote. Saldo disponible: " + saldoAnterior);
            }
            saldoPosterior = saldoAnterior - cantidad;
            lote.setCantidadActual(saldoPosterior);

            if (saldoPosterior == 0) {
                lote.setEstado("AGOTADO");
            }
        } else {
            throw new IllegalArgumentException("Tipo de ajuste no reconocido: " + tipoAjuste);
        }

        loteRepository.save(lote);

        MovimientoInventario mov = new MovimientoInventario(
                lote.getMedicamento(),
                lote,
                tipoAjuste.toUpperCase(),
                cantidad,
                saldoAnterior,
                saldoPosterior,
                (motivo != null && !motivo.isBlank()) ? motivo : "AJUSTE_MANUAL"
        );
        movimientoRepo.save(mov);
    }
}
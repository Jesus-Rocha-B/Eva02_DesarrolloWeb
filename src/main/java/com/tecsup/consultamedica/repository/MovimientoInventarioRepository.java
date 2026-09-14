package com.tecsup.consultamedica.repository;

import com.tecsup.consultamedica.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // Auditoría global ordenada cronológicamente (más reciente primero)
    List<MovimientoInventario> findAllByOrderByFechaHoraDesc();

    // Historial filtrado por medicamento específico
    List<MovimientoInventario> findByMedicamentoIdOrderByFechaHoraDesc(Long medicamentoId);

    // RF-FAR-01: Contar dispensaciones ejecutadas en un rango de fecha/hora (para el día actual)
    long countByTipoMovimientoAndFechaHoraBetween(
            String tipoMovimiento,
            java.time.LocalDateTime inicio,
            java.time.LocalDateTime fin);
}
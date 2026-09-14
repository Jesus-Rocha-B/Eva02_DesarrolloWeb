package com.tecsup.consultamedica.repository;

import com.tecsup.consultamedica.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {

    List<Lote> findByMedicamentoId(Long medicamentoId);

    List<Lote> findByEstado(String estado);

    List<Lote> findByMedicamentoIdAndEstadoOrderByFechaVencimientoAsc(Long medicamentoId, String estado);

    // Rango preventivo para alertas
    List<Lote> findByFechaVencimientoBetweenAndEstadoOrderByFechaVencimientoAsc(
            LocalDate desde, LocalDate hasta, String estado);

    @Query("SELECT COALESCE(SUM(l.cantidadActual), 0) FROM Lote l WHERE l.medicamento.id = :medicamentoId AND l.estado = 'DISPONIBLE'")
    Integer obtenerStockTotalPorMedicamento(@Param("medicamentoId") Long medicamentoId);

    // <-- [ESTE ES EL MÉTODO QUE FALTA] RF-FAR-14: Obtiene lotes vigentes ordenados por FEFO
    List<Lote> findByMedicamentoIdAndEstadoAndFechaVencimientoGreaterThanEqualOrderByFechaVencimientoAsc(
            Long medicamentoId, String estado, LocalDate fechaVencimiento);

    // Sumatoria de stock útil no vencido
    @Query("SELECT COALESCE(SUM(l.cantidadActual), 0) FROM Lote l " +
            "WHERE l.medicamento.id = :medicamentoId " +
            "AND l.estado = 'DISPONIBLE' " +
            "AND l.fechaVencimiento >= :fechaActual")
    Integer obtenerStockVigentePorMedicamento(
            @Param("medicamentoId") Long medicamentoId,
            @Param("fechaActual") LocalDate fechaActual);
}
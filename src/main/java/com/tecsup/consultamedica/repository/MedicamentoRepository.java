package com.tecsup.consultamedica.repository;
import com.tecsup.consultamedica.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    Optional<Medicamento> findByCodigo(String codigo);
    long countByEstado(String estado);
}
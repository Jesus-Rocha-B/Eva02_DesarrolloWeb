package com.tecsup.consultamedica.repository;

import com.tecsup.consultamedica.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findAllByOrderByFechaHoraDesc();
    long countByOperacion(String operacion);
}

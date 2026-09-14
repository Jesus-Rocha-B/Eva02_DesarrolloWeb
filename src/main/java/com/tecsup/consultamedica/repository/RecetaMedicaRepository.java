package com.tecsup.consultamedica.repository;

import com.tecsup.consultamedica.model.RecetaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaMedicaRepository extends JpaRepository<RecetaMedica, Long> {
    List<RecetaMedica> findByEstadoOrderByFechaEmisionDesc(String estado);
    List<RecetaMedica> findAllByOrderByFechaEmisionDesc();
    Optional<RecetaMedica> findByNumeroReceta(String numeroReceta);
    long countByEstado(String estado);
}
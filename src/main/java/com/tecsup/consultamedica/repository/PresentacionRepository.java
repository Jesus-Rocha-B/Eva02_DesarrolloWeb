package com.tecsup.consultamedica.repository;
import com.tecsup.consultamedica.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentacionRepository extends JpaRepository<Presentacion, Long> {
}
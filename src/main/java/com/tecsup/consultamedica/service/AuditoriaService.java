package com.tecsup.consultamedica.service;

import com.tecsup.consultamedica.model.Auditoria;
import com.tecsup.consultamedica.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarAuditoria(String usuario, String operacion, String entidad, Long registroId, String detalles) {
        Auditoria auditoria = new Auditoria(usuario, LocalDateTime.now(), operacion, entidad, registroId, detalles);
        auditoriaRepository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public List<Auditoria> listarTodas() {
        return auditoriaRepository.findAllByOrderByFechaHoraDesc();
    }

    @Transactional(readOnly = true)
    public long contarTotal() {
        return auditoriaRepository.count();
    }

    @Transactional(readOnly = true)
    public long contarPorOperacion(String operacion) {
        return auditoriaRepository.countByOperacion(operacion);
    }
}

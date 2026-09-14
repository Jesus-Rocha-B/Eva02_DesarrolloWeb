package com.tecsup.consultamedica.service;

import com.tecsup.consultamedica.model.DetalleReceta;
import com.tecsup.consultamedica.model.Paciente;
import com.tecsup.consultamedica.model.RecetaMedica;
import com.tecsup.consultamedica.repository.MedicamentoRepository;
import com.tecsup.consultamedica.repository.PacienteRepository;
import com.tecsup.consultamedica.repository.RecetaMedicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecetaMedicaService {

    private final RecetaMedicaRepository recetaRepo;
    private final PacienteRepository pacienteRepo;
    private final MedicamentoRepository medicamentoRepo;
    private final LoteService loteService;

    public RecetaMedicaService(RecetaMedicaRepository recetaRepo,
                               PacienteRepository pacienteRepo,
                               MedicamentoRepository medicamentoRepo,
                               LoteService loteService) {
        this.recetaRepo = recetaRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicamentoRepo = medicamentoRepo;
        this.loteService = loteService;
    }

    public List<RecetaMedica> listarTodas() {
        return recetaRepo.findAllByOrderByFechaEmisionDesc();
    }

    public List<RecetaMedica> listarPendientes() {
        return recetaRepo.findByEstadoOrderByFechaEmisionDesc("PENDIENTE");
    }

    public RecetaMedica buscarPorId(Long id) {
        return recetaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
    }

    @Transactional
    public RecetaMedica emitirReceta(String dniPaciente, String nombrePaciente, String historiaClinica,
                                     String medicoNombre, String medicoColegiatura, String diagnostico,
                                     Long medicamentoId, String dosis, String frecuencia,
                                     String duracion, String via, Integer cantidad, String indicaciones) {

        // <-- [NUEVO / VALIDACIÓN]: Validación estricta de parámetros en Backend
        if (dniPaciente == null || !dniPaciente.trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe contener exactamente 8 dígitos numéricos.");
        }
        if (nombrePaciente == null || nombrePaciente.trim().length() < 3 || nombrePaciente.trim().length() > 80) {
            throw new IllegalArgumentException("El nombre del paciente debe tener entre 3 y 80 caracteres.");
        }
        if (historiaClinica == null || !historiaClinica.trim().matches("HC-\\d{4}-\\d{4,6}")) {
            throw new IllegalArgumentException("El formato de Historia Clínica es inválido. Patrón obligatorio: HC-AAAA-NNNN (ej. HC-2026-0014).");
        }
        if (medicoNombre == null || medicoNombre.trim().length() < 3 || medicoNombre.trim().length() > 80) {
            throw new IllegalArgumentException("El nombre del médico debe tener entre 3 y 80 caracteres.");
        }
        if (medicoColegiatura == null || !medicoColegiatura.trim().matches("CMP-\\d{4,6}")) {
            throw new IllegalArgumentException("El CMP debe seguir el formato oficial CMP-XXXXX (ej. CMP-45812).");
        }
        if (cantidad == null || cantidad <= 0 || cantidad > 120) {
            throw new IllegalArgumentException("La cantidad recetada debe situarse entre 1 y 120 unidades.");
        }

        // 1. Obtener o registrar automáticamente al paciente saneando cadenas
        String dniSanitizado = dniPaciente.trim();
        Paciente paciente = pacienteRepo.findByNumeroDocumento(dniSanitizado)
                .orElseGet(() -> {
                    Paciente p = new Paciente();
                    p.setNumeroDocumento(dniSanitizado);
                    p.setNombreCompleto(nombrePaciente.trim());
                    p.setNumeroHistoria(historiaClinica.trim());
                    return pacienteRepo.save(p);
                });

        // 2. Construir cabecera de la receta
        RecetaMedica receta = new RecetaMedica();
        receta.setNumeroReceta("REC-" + System.currentTimeMillis() % 1000000);
        receta.setPaciente(paciente);
        receta.setMedicoNombre(medicoNombre.trim());
        receta.setMedicoColegiatura(medicoColegiatura.trim());
        receta.setDiagnostico(diagnostico != null ? diagnostico.trim() : null);
        receta.setFechaEmision(LocalDateTime.now());
        receta.setEstado("PENDIENTE");

        // 3. Asociar detalle de prescripción
        DetalleReceta detalle = new DetalleReceta();
        detalle.setMedicamento(medicamentoRepo.findById(medicamentoId)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + medicamentoId)));
        detalle.setDosis(dosis.trim());
        detalle.setFrecuencia(frecuencia.trim());
        detalle.setDuracion(duracion.trim());
        detalle.setViaAdministracion(via.trim());
        detalle.setCantidad(cantidad);
        detalle.setIndicaciones(indicaciones != null && !indicaciones.isBlank() ? indicaciones.trim() : null);

        receta.agregarDetalle(detalle);

        return recetaRepo.save(receta);
    }

    @Transactional
    public void dispensarReceta(Long recetaId) {
        RecetaMedica receta = buscarPorId(recetaId);

        if (!"PENDIENTE".equalsIgnoreCase(receta.getEstado())) {
            throw new IllegalStateException("La receta N° " + receta.getNumeroReceta() + " ya no está pendiente de dispensación.");
        }

        // Ejecutar salida FEFO y validación de vigencia por cada medicamento recetado
        for (DetalleReceta detalle : receta.getDetalles()) {
            loteService.dispensarMedicamento(detalle.getMedicamento().getId(), detalle.getCantidad());
        }

        receta.setEstado("DISPENSADA");
        recetaRepo.save(receta);
    }
}
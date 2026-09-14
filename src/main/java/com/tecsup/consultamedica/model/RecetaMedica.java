package com.tecsup.consultamedica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receta_medica")
public class RecetaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_receta", nullable = false, unique = true, length = 20)
    private String numeroReceta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "medico_nombre", nullable = false, length = 150)
    private String medicoNombre;

    @Column(name = "medico_colegiatura", length = 20)
    private String medicoColegiatura;

    @Column(length = 255)
    private String diagnostico;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleReceta> detalles = new ArrayList<>();

    public RecetaMedica() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroReceta() { return numeroReceta; }
    public void setNumeroReceta(String numeroReceta) { this.numeroReceta = numeroReceta; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }

    public String getMedicoColegiatura() { return medicoColegiatura; }
    public void setMedicoColegiatura(String medicoColegiatura) { this.medicoColegiatura = medicoColegiatura; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetalleReceta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleReceta> detalles) { this.detalles = detalles; }

    public void agregarDetalle(DetalleReceta detalle) {
        detalles.add(detalle);
        detalle.setReceta(this);
    }
}
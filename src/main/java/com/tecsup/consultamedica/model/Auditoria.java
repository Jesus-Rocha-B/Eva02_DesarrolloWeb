package com.tecsup.consultamedica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 30)
    private String operacion; // REGISTRO, MODIFICACION, DESACTIVACION

    @Column(nullable = false, length = 60)
    private String entidad; // Usuario, Rol, Medicamento, Lote

    @Column(name = "registro_id")
    private Long registroId;

    @Column(length = 255)
    private String detalles;

    public Auditoria() {}

    public Auditoria(String usuario, LocalDateTime fechaHora, String operacion, String entidad, Long registroId, String detalles) {
        this.usuario = usuario;
        this.fechaHora = fechaHora;
        this.operacion = operacion;
        this.entidad = entidad;
        this.registroId = registroId;
        this.detalles = detalles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }

    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }

    public Long getRegistroId() { return registroId; }
    public void setRegistroId(Long registroId) { this.registroId = registroId; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public String getFechaFormateada() {
        if (fechaHora == null) return "-";
        return fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}

package com.tecsup.consultamedica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento; // ENTRADA o SALIDA

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "saldo_anterior", nullable = false)
    private Integer saldoAnterior;

    @Column(name = "saldo_posterior", nullable = false)
    private Integer saldoPosterior;

    @Column(nullable = false, length = 100)
    private String motivo;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(length = 50)
    private String usuario;

    @PrePersist
    public void prePersist() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
        if (this.usuario == null) {
            this.usuario = "FARMACIA";
        }
    }

    public MovimientoInventario() {}

    public MovimientoInventario(Medicamento medicamento, Lote lote, String tipoMovimiento,
                                Integer cantidad, Integer saldoAnterior, Integer saldoPosterior,
                                String motivo) {
        this.medicamento = medicamento;
        this.lote = lote;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.motivo = motivo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public Lote getLote() { return lote; }
    public void setLote(Lote lote) { this.lote = lote; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Integer getSaldoAnterior() { return saldoAnterior; }
    public void setSaldoAnterior(Integer saldoAnterior) { this.saldoAnterior = saldoAnterior; }

    public Integer getSaldoPosterior() { return saldoPosterior; }
    public void setSaldoPosterior(Integer saldoPosterior) { this.saldoPosterior = saldoPosterior; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
}
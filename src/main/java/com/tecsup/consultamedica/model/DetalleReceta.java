package com.tecsup.consultamedica.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_receta")
public class DetalleReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receta_id", nullable = false)
    private RecetaMedica receta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    @Column(nullable = false, length = 100)
    private String dosis;

    @Column(nullable = false, length = 100)
    private String frecuencia;

    @Column(nullable = false, length = 50)
    private String duracion;

    @Column(name = "via_administracion", nullable = false, length = 50)
    private String viaAdministracion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(columnDefinition = "TEXT")
    private String indicaciones;

    public DetalleReceta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RecetaMedica getReceta() { return receta; }
    public void setReceta(RecetaMedica receta) { this.receta = receta; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getViaAdministracion() { return viaAdministracion; }
    public void setViaAdministracion(String viaAdministracion) { this.viaAdministracion = viaAdministracion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }
}
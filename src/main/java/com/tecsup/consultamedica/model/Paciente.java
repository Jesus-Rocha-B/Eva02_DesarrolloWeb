package com.tecsup.consultamedica.model;

import jakarta.persistence.*;

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento = "DNI";

    @Column(name = "numero_documento", nullable = false, unique = true, length = 15)
    private String numeroDocumento;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "numero_historia", nullable = false, unique = true, length = 20)
    private String numeroHistoria;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    public Paciente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getNumeroHistoria() { return numeroHistoria; }
    public void setNumeroHistoria(String numeroHistoria) { this.numeroHistoria = numeroHistoria; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
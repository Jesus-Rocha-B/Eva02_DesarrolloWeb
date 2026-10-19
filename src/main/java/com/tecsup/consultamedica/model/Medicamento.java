package com.tecsup.consultamedica.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicamento")
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;
    @Column(nullable = false, length = 150)
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "principio_activo_id", nullable = false)
    private PrincipioActivo principioActivo;
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaMedicamento categoria;
    @ManyToOne
    @JoinColumn(name = "presentacion_id", nullable = false)
    private Presentacion presentacion;
    @ManyToOne
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;
    @Column(length = 255)
    private String descripcion;
    @Column(name = "requiere_receta", nullable = false)
    private boolean requiereReceta;
    @Column(nullable = false, length = 10)
    private String estado;
    @OneToMany(mappedBy = "medicamento")
    private List<Lote> lotes = new ArrayList<>();

    public Medicamento() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public PrincipioActivo getPrincipioActivo() {
        return principioActivo;
    }
    public void setPrincipioActivo(PrincipioActivo principioActivo) {
        this.principioActivo = principioActivo;
    }
    public CategoriaMedicamento getCategoria() {
        return categoria;
    }
    public void setCategoria(CategoriaMedicamento categoria) {
        this.categoria = categoria;
    }
    public Presentacion getPresentacion() {
        return presentacion;
    }
    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }
    public Laboratorio getLaboratorio() {
        return laboratorio;
    }
    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public boolean isRequiereReceta() {
        return requiereReceta;
    }
    public void setRequiereReceta(boolean requiereReceta) {
        this.requiereReceta = requiereReceta;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public List<Lote> getLotes() {
        return lotes;
    }
    public void setLotes(List<Lote> lotes) {
        this.lotes = lotes;
    }
}
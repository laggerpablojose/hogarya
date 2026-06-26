package tuti.desi.hogarya.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "propiedades")
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    @Column(nullable = false, length = 200)
    private String direccion;

    @NotBlank(message = "El barrio o zona es obligatorio")
    @Size(max = 100, message = "El barrio/zona no puede superar los 100 caracteres")
    @Column(name = "barrio_zona", nullable = false, length = 100)
    private String barrioZona;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String ciudad;

    @NotNull(message = "El tipo de propiedad es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoPropiedad tipo;

    @NotNull(message = "La cantidad de ambientes es obligatoria")
    @Positive(message = "La cantidad de ambientes debe ser mayor a cero")
    @Column(name = "cantidad_ambientes", nullable = false)
    private Integer cantidadAmbientes;

    @NotNull(message = "Los metros cuadrados son obligatorios")
    @Positive(message = "Los metros cuadrados deben ser mayores a cero")
    @Column(name = "metros_cuadrados", nullable = false)
    private Double metrosCuadrados;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    @Column(length = 1000)
    private String descripcion;

    @Size(max = 500, message = "Las comodidades no pueden superar los 500 caracteres")
    @Column(length = 500)
    private String comodidades;

    @Size(max = 1000, message = "Las URLs de fotos no pueden superar los 1000 caracteres")
    @Column(name = "fotos_urls", length = 1000)
    private String fotosUrls;

    @NotNull(message = "El estado de la propiedad es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_disponible", nullable = false, length = 20)
    private EstadoPropiedad estadoDisponible = EstadoPropiedad.DISPONIBLE;

    @NotNull(message = "El propietario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public Propiedad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrioZona() {
        return barrioZona;
    }

    public void setBarrioZona(String barrioZona) {
        this.barrioZona = barrioZona;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public TipoPropiedad getTipo() {
        return tipo;
    }

    public void setTipo(TipoPropiedad tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidadAmbientes() {
        return cantidadAmbientes;
    }

    public void setCantidadAmbientes(Integer cantidadAmbientes) {
        this.cantidadAmbientes = cantidadAmbientes;
    }

    public Double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(Double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComodidades() {
        return comodidades;
    }

    public void setComodidades(String comodidades) {
        this.comodidades = comodidades;
    }

    public String getFotosUrls() {
        return fotosUrls;
    }

    public void setFotosUrls(String fotosUrls) {
        this.fotosUrls = fotosUrls;
    }

    public EstadoPropiedad getEstadoDisponible() {
        return estadoDisponible;
    }

    public void setEstadoDisponible(EstadoPropiedad estadoDisponible) {
        this.estadoDisponible = estadoDisponible;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}

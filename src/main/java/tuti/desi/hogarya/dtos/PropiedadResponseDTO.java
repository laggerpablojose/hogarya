package tuti.desi.hogarya.dtos;

import java.math.BigDecimal;

import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.entidades.TipoPropiedad;

/**
 * DTO de salida. Expone los datos de la Propiedad junto con un resumen
 * del propietario, sin devolver la entidad Persona completa.
 */
public class PropiedadResponseDTO {

    private Long id;
    private String direccion;
    private String ciudad;
    private TipoPropiedad tipo;
    private Integer ambientes;
    private BigDecimal metrosCuadrados;
    private String descripcion;
    private EstadoPropiedad estado;
    private Long propietarioId;
    private String propietarioNombreCompleto;

    public PropiedadResponseDTO() {
    }

    public PropiedadResponseDTO(Propiedad propiedad) {
        this.id = propiedad.getId();
        this.direccion = propiedad.getDireccion();
        this.ciudad = propiedad.getCiudad();
        this.tipo = propiedad.getTipo();
        this.ambientes = propiedad.getAmbientes();
        this.metrosCuadrados = propiedad.getMetrosCuadrados();
        this.descripcion = propiedad.getDescripcion();
        this.estado = propiedad.getEstado();
        if (propiedad.getPropietario() != null) {
            this.propietarioId = propiedad.getPropietario().getId();
            this.propietarioNombreCompleto = propiedad.getPropietario().getNombre()
                    + " " + propiedad.getPropietario().getApellido();
        }
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

    public Integer getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(Integer ambientes) {
        this.ambientes = ambientes;
    }

    public BigDecimal getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(BigDecimal metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoPropiedad getEstado() {
        return estado;
    }

    public void setEstado(EstadoPropiedad estado) {
        this.estado = estado;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Long propietarioId) {
        this.propietarioId = propietarioId;
    }

    public String getPropietarioNombreCompleto() {
        return propietarioNombreCompleto;
    }

    public void setPropietarioNombreCompleto(String propietarioNombreCompleto) {
        this.propietarioNombreCompleto = propietarioNombreCompleto;
    }
}

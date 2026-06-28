package tuti.desi.hogarya.dtos;

import java.time.LocalDate;

import tuti.desi.hogarya.entidades.InteresVisita;

public class InteresVisitaResponseDTO {

    private Long id;
    private String observacion;
    private LocalDate fecha;
    private Long interesadoId;
    private String interesadoNombreCompleto;
    private Long propiedadId;
    private String propiedadDireccion;

    public InteresVisitaResponseDTO() {
    }

    public InteresVisitaResponseDTO(InteresVisita visita) {
        this.id = visita.getId();
        this.observacion = visita.getObservacion();
        this.fecha = visita.getFecha();
        if (visita.getInteresado() != null) {
            this.interesadoId = visita.getInteresado().getId();
            this.interesadoNombreCompleto = visita.getInteresado().getNombre()
                    + " " + visita.getInteresado().getApellido();
        }
        if (visita.getPropiedad() != null) {
            this.propiedadId = visita.getPropiedad().getId();
            this.propiedadDireccion = visita.getPropiedad().getDireccion();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getInteresadoId() {
        return interesadoId;
    }

    public void setInteresadoId(Long interesadoId) {
        this.interesadoId = interesadoId;
    }

    public String getInteresadoNombreCompleto() {
        return interesadoNombreCompleto;
    }

    public void setInteresadoNombreCompleto(String interesadoNombreCompleto) {
        this.interesadoNombreCompleto = interesadoNombreCompleto;
    }

    public Long getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
    }

    public String getPropiedadDireccion() {
        return propiedadDireccion;
    }

    public void setPropiedadDireccion(String propiedadDireccion) {
        this.propiedadDireccion = propiedadDireccion;
    }
}

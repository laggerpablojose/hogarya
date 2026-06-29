package tuti.desi.hogarya.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InteresVisitaRequestDTO {

    @Size(max = 500, message = "La observación no puede superar los 500 caracteres")
    private String observacion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El id del interesado es obligatorio")
    private Long interesadoId;

    @NotNull(message = "El id de la propiedad es obligatorio")
    private Long propiedadId;

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

    public Long getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
    }
}

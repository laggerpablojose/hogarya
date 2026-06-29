package tuti.desi.hogarya.dtos;

import tuti.desi.hogarya.entidades.Interesado;

public class InteresadoResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String contacto;

    public InteresadoResponseDTO() {
    }

    public InteresadoResponseDTO(Interesado interesado) {
        this.id = interesado.getId();
        this.nombre = interesado.getNombre();
        this.apellido = interesado.getApellido();
        this.contacto = interesado.getContacto();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}

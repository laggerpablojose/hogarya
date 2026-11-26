package com.hogarya.presentacion;

import org.springframework.hateoas.RepresentationModel;

public class InicioResponse extends RepresentationModel<InicioResponse> {

    private final String nombre;

    public InicioResponse(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

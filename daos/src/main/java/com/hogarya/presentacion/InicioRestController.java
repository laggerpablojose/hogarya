package com.hogarya.presentacion;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class InicioRestController {

    @GetMapping(value = "/", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Consultar el punto de entrada de la API")
    public InicioResponse consultarInicio() {
        InicioResponse response = new InicioResponse("HogarYA");
        response.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(InicioRestController.class).consultarInicio())
                .withSelfRel());
        response.add(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v3/api-docs").toUriString(), "documentacion"));
        response.add(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/swagger-ui/index.html").toUriString(), "swagger"));
        return response;
    }
}

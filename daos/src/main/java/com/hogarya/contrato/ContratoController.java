package com.hogarya.contrato;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/** Solo lectura: existe para que funcionen los enlaces HATEOAS "contrato". */
@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService service;

    @GetMapping("/{id}")
    public EntityModel<ContratoResponse> obtener(@PathVariable Long id) {
        return EntityModel.of(service.obtener(id),
                linkTo(methodOn(ContratoController.class).obtener(id)).withSelfRel());
    }
}

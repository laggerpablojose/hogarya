package com.hogarya.pago;

import com.hogarya.factura.FacturaController;
import com.hogarya.pago.dto.PagoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoResponse, EntityModel<PagoResponse>> {

    @Override
    public EntityModel<PagoResponse> toModel(PagoResponse p) {
        return EntityModel.of(p,
                linkTo(methodOn(PagoController.class).obtener(p.id())).withSelfRel(),
                linkTo(methodOn(FacturaController.class).obtener(p.facturaId())).withRel("factura"));
    }
}

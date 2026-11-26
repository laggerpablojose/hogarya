package com.hogarya.factura;

import com.hogarya.contrato.ContratoController;
import com.hogarya.factura.dto.FacturaResponse;
import com.hogarya.pago.PagoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FacturaModelAssembler
        implements RepresentationModelAssembler<FacturaResponse, EntityModel<FacturaResponse>> {

    @Override
    public EntityModel<FacturaResponse> toModel(FacturaResponse f) {
        EntityModel<FacturaResponse> model = EntityModel.of(f,
                linkTo(methodOn(FacturaController.class).obtener(f.id())).withSelfRel(),
                linkTo(methodOn(ContratoController.class).obtener(f.contratoId())).withRel("contrato"));

        // Enlace "pago" unicamente cuando existe
        if (f.pagoId() != null) {
            model.add(linkTo(methodOn(PagoController.class).obtener(f.pagoId())).withRel("pago"));
        }
        return model;
    }
}

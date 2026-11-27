package com.hogarya.informe;

import com.hogarya.incidente.IncidenteController;
import com.hogarya.informe.dto.InformeResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InformeModelAssembler
        implements RepresentationModelAssembler<InformeResponse, EntityModel<InformeResponse>> {

    @Override
    public EntityModel<InformeResponse> toModel(InformeResponse i) {
        return EntityModel.of(i,
                linkTo(methodOn(InformeResolucionController.class).obtener(i.id())).withSelfRel(),
                linkTo(methodOn(IncidenteController.class).obtener(i.incidenteId())).withRel("incidente"));
    }
}

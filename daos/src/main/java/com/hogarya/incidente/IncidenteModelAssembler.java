package com.hogarya.incidente;

import com.hogarya.contrato.ContratoController;
import com.hogarya.incidente.dto.IncidenteResponse;
import com.hogarya.informe.InformeResolucionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IncidenteModelAssembler
        implements RepresentationModelAssembler<IncidenteResponse, EntityModel<IncidenteResponse>> {

    @Override
    public EntityModel<IncidenteResponse> toModel(IncidenteResponse i) {
        // informes: GET /informes-resolucion?incidenteId={id}
        String informes = linkTo(InformeResolucionController.class).toUriComponentsBuilder()
                .queryParam("incidenteId", i.id())
                .toUriString();

        return EntityModel.of(i,
                linkTo(methodOn(IncidenteController.class).obtener(i.id())).withSelfRel(),
                linkTo(methodOn(ContratoController.class).obtener(i.contratoId())).withRel("contrato"),
                Link.of(informes, "informes"),
                linkTo(methodOn(IncidenteController.class).historial(i.id())).withRel("historial"));
    }
}

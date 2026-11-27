package com.hogarya.informe;

import com.hogarya.informe.dto.InformeFiltro;
import com.hogarya.informe.dto.InformeResponse;
import com.hogarya.informe.dto.ResolverIncidenteRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/informes-resolucion")
@RequiredArgsConstructor
public class InformeResolucionController {

    private final InformeResolucionService service;
    private final InformeModelAssembler assembler;

    /** Resuelve el incidente y registra su informe (operacion atomica). */
    @Operation(summary = "Resolver un incidente registrando informe, estado e historial")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recurso creado con enlace self y encabezado Location"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado inexistente"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio o concurrencia")
    })
    @PostMapping
    public ResponseEntity<EntityModel<InformeResponse>> resolver(@Valid @RequestBody ResolverIncidenteRequest req) {
        EntityModel<InformeResponse> model = assembler.toModel(service.resolver(req));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/{id}")
    public EntityModel<InformeResponse> obtener(@PathVariable Long id) {
        return assembler.toModel(service.obtener(id));
    }

    /** Filtros: ?incidenteId=1&responsable=perez&fechaDesde=2026-11-01&fechaHasta=2026-11-30 */
    @GetMapping
    public CollectionModel<EntityModel<InformeResponse>> listar(@ParameterObject @ModelAttribute InformeFiltro filtro) {
        return assembler.toCollectionModel(service.listar(filtro))
                .add(Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()).withSelfRel());
    }
}

package com.hogarya.incidente;

import com.hogarya.incidente.dto.*;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/incidentes")
@RequiredArgsConstructor
public class IncidenteController {

    private final IncidenteService service;
    private final IncidenteModelAssembler assembler;

    @Operation(summary = "Registrar un incidente y su historial inicial")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recurso creado con enlace self y encabezado Location"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado inexistente"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio o concurrencia")
    })
    @PostMapping
    public ResponseEntity<EntityModel<IncidenteResponse>> registrar(@Valid @RequestBody RegistrarIncidenteRequest req) {
        EntityModel<IncidenteResponse> model = assembler.toModel(service.registrar(req));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/{id}")
    public EntityModel<IncidenteResponse> obtener(@PathVariable Long id) {
        return assembler.toModel(service.obtener(id));
    }

    /** Filtros: ?contratoId=1&estado=ABIERTO&categoria=GAS&prioridad=ALTA&fechaDesde=2026-11-01&fechaHasta=2026-11-30 */
    @GetMapping
    public CollectionModel<EntityModel<IncidenteResponse>> listar(@ParameterObject @ModelAttribute IncidenteFiltro filtro) {
        return assembler.toCollectionModel(service.listar(filtro))
                .add(Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()).withSelfRel());
    }

    /** Body: { "estado": "EN_PROCESO" } */
    @Operation(summary = "Cambiar el estado de un incidente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso inexistente"),
            @ApiResponse(responseCode = "409", description = "Transición no permitida o conflicto concurrente")
    })
    @PatchMapping("/{id}/estado")
    public EntityModel<IncidenteResponse> cambiarEstado(@PathVariable Long id,
                                                        @Valid @RequestBody CambiarEstadoIncidenteRequest req) {
        return assembler.toModel(service.cambiarEstado(id, req));
    }

    /** Historial de solo lectura, ordenado cronologicamente. */
    @GetMapping("/{id}/historial")
    public CollectionModel<HistorialEstadoResponse> historial(@PathVariable Long id) {
        return CollectionModel.of(service.historial(id),
                linkTo(methodOn(IncidenteController.class).historial(id)).withSelfRel(),
                linkTo(methodOn(IncidenteController.class).obtener(id)).withRel("incidente"));
    }
}

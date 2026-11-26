package com.hogarya.factura;

import com.hogarya.factura.dto.CambiarEstadoFacturaRequest;
import com.hogarya.factura.dto.EmitirFacturaRequest;
import com.hogarya.factura.dto.FacturaFiltro;
import com.hogarya.factura.dto.FacturaResponse;
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
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService service;
    private final FacturaModelAssembler assembler;

    @Operation(summary = "Emitir una factura de alquiler")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recurso creado con enlace self y encabezado Location"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado inexistente"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio o concurrencia")
    })
    @PostMapping
    public ResponseEntity<EntityModel<FacturaResponse>> emitir(@Valid @RequestBody EmitirFacturaRequest req) {
        EntityModel<FacturaResponse> model = assembler.toModel(service.emitir(req));
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/{id}")
    public EntityModel<FacturaResponse> obtener(@PathVariable Long id) {
        return assembler.toModel(service.obtener(id));
    }

    /** Filtros: ?contratoId=1&periodo=2026-11&estado=VENCIDA&tipo=MENSUAL */
    @GetMapping
    public CollectionModel<EntityModel<FacturaResponse>> listar(@ParameterObject @ModelAttribute FacturaFiltro filtro) {
        return assembler.toCollectionModel(service.listar(filtro))
                .add(Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()).withSelfRel());
    }

    /** Body: { "estado": "ANULADA" } */
    @Operation(summary = "Anular una factura pendiente o vencida")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso inexistente"),
            @ApiResponse(responseCode = "409", description = "Transición no permitida o conflicto concurrente")
    })
    @PatchMapping("/{id}/estado")
    public EntityModel<FacturaResponse> cambiarEstado(@PathVariable Long id,
                                                      @Valid @RequestBody CambiarEstadoFacturaRequest req) {
        return assembler.toModel(service.cambiarEstado(id, req));
    }
}

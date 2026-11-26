package com.hogarya.pago;

import com.hogarya.pago.dto.PagoFiltro;
import com.hogarya.pago.dto.PagoResponse;
import com.hogarya.pago.dto.RegistrarPagoRequest;
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
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;
    private final PagoModelAssembler assembler;

    @Operation(summary = "Registrar el pago total de una factura")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recurso creado con enlace self y encabezado Location"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado inexistente"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio o concurrencia")
    })
    @PostMapping
    public ResponseEntity<EntityModel<PagoResponse>> registrar(@Valid @RequestBody RegistrarPagoRequest req) {
        EntityModel<PagoResponse> model = assembler.toModel(service.registrar(req));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/{id}")
    public EntityModel<PagoResponse> obtener(@PathVariable Long id) {
        return assembler.toModel(service.obtener(id));
    }

    /** Filtros: ?facturaId=1&medioPago=TRANSFERENCIA&fechaDesde=2026-11-01&fechaHasta=2026-11-30 */
    @GetMapping
    public CollectionModel<EntityModel<PagoResponse>> listar(@ParameterObject @ModelAttribute PagoFiltro filtro) {
        return assembler.toCollectionModel(service.listar(filtro))
                .add(Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()).withSelfRel());
    }
}

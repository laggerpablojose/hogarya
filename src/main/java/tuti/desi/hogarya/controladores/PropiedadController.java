package tuti.desi.hogarya.controladores;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tuti.desi.hogarya.dtos.PropiedadRequestDTO;
import tuti.desi.hogarya.dtos.PropiedadResponseDTO;
import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.servicios.PropiedadService;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController {

    private final PropiedadService propiedadService;

    public PropiedadController(PropiedadService propiedadService) {
        this.propiedadService = propiedadService;
    }

    @GetMapping
    public ResponseEntity<List<PropiedadResponseDTO>> listar(
            @RequestParam(required = false) EstadoPropiedad estado,
            @RequestParam(required = false) String ciudad) {

        List<Propiedad> propiedades;
        if (estado != null) {
            propiedades = propiedadService.buscarPorEstado(estado);
        } else if (ciudad != null && !ciudad.isBlank()) {
            propiedades = propiedadService.buscarPorCiudad(ciudad);
        } else {
            propiedades = propiedadService.listarTodas();
        }

        List<PropiedadResponseDTO> respuesta = propiedades.stream()
                .map(PropiedadResponseDTO::new)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadResponseDTO> buscarPorId(@PathVariable Long id) {
        Propiedad propiedad = propiedadService.buscarPorId(id);
        return ResponseEntity.ok(new PropiedadResponseDTO(propiedad));
    }

    @PostMapping
    public ResponseEntity<PropiedadResponseDTO> crear(@Valid @RequestBody PropiedadRequestDTO dto) {
        Propiedad creada = propiedadService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PropiedadResponseDTO(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody PropiedadRequestDTO dto) {
        Propiedad actualizada = propiedadService.actualizar(id, dto);
        return ResponseEntity.ok(new PropiedadResponseDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        propiedadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

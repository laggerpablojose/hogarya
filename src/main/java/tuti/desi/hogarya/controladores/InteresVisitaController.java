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

import tuti.desi.hogarya.dtos.InteresVisitaRequestDTO;
import tuti.desi.hogarya.dtos.InteresVisitaResponseDTO;
import tuti.desi.hogarya.entidades.InteresVisita;
import tuti.desi.hogarya.servicios.InteresVisitaService;

@RestController
@RequestMapping("/api/intereses-visitas")
public class InteresVisitaController {

    private final InteresVisitaService interesVisitaService;

    public InteresVisitaController(InteresVisitaService interesVisitaService) {
        this.interesVisitaService = interesVisitaService;
    }

    @GetMapping
    public ResponseEntity<List<InteresVisitaResponseDTO>> listar(
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long interesadoId) {

        List<InteresVisita> visitas;
        if (propiedadId != null) {
            visitas = interesVisitaService.buscarPorPropiedad(propiedadId);
        } else if (interesadoId != null) {
            visitas = interesVisitaService.buscarPorInteresado(interesadoId);
        } else {
            visitas = interesVisitaService.listarTodas();
        }

        List<InteresVisitaResponseDTO> respuesta = visitas.stream()
                .map(InteresVisitaResponseDTO::new)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InteresVisitaResponseDTO> buscarPorId(@PathVariable Long id) {
        InteresVisita visita = interesVisitaService.buscarPorId(id);
        return ResponseEntity.ok(new InteresVisitaResponseDTO(visita));
    }

    @PostMapping
    public ResponseEntity<InteresVisitaResponseDTO> crear(@Valid @RequestBody InteresVisitaRequestDTO dto) {
        InteresVisita creada = interesVisitaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new InteresVisitaResponseDTO(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InteresVisitaResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody InteresVisitaRequestDTO dto) {
        InteresVisita actualizada = interesVisitaService.actualizar(id, dto);
        return ResponseEntity.ok(new InteresVisitaResponseDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        interesVisitaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

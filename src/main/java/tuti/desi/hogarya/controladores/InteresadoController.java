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
import org.springframework.web.bind.annotation.RestController;

import tuti.desi.hogarya.dtos.InteresadoRequestDTO;
import tuti.desi.hogarya.dtos.InteresadoResponseDTO;
import tuti.desi.hogarya.entidades.Interesado;
import tuti.desi.hogarya.servicios.InteresadoService;

@RestController
@RequestMapping("/api/interesados")
public class InteresadoController {

    private final InteresadoService interesadoService;

    public InteresadoController(InteresadoService interesadoService) {
        this.interesadoService = interesadoService;
    }

    @GetMapping
    public ResponseEntity<List<InteresadoResponseDTO>> listar() {
        List<InteresadoResponseDTO> respuesta = interesadoService.listarTodos().stream()
                .map(InteresadoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InteresadoResponseDTO> buscarPorId(@PathVariable Long id) {
        Interesado interesado = interesadoService.buscarPorId(id);
        return ResponseEntity.ok(new InteresadoResponseDTO(interesado));
    }

    @PostMapping
    public ResponseEntity<InteresadoResponseDTO> crear(@Valid @RequestBody InteresadoRequestDTO dto) {
        Interesado creado = interesadoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new InteresadoResponseDTO(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InteresadoResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody InteresadoRequestDTO dto) {
        Interesado actualizado = interesadoService.actualizar(id, dto);
        return ResponseEntity.ok(new InteresadoResponseDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        interesadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

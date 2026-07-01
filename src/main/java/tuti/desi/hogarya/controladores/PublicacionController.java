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

import tuti.desi.hogarya.dtos.PublicacionRequestDTO;
import tuti.desi.hogarya.dtos.PublicacionResponseDTO;
import tuti.desi.hogarya.entidades.EstadoPublicacion;
import tuti.desi.hogarya.entidades.Publicacion;
import tuti.desi.hogarya.servicios.PublicacionService;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public ResponseEntity<List<PublicacionResponseDTO>> listar(
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) EstadoPublicacion estado,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {

        List<Publicacion> publicaciones = publicacionService.listar(propiedadId, ciudad, estado, precioMin, precioMax);

        List<PublicacionResponseDTO> respuesta = publicaciones.stream()
                .map(PublicacionResponseDTO::new)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponseDTO> buscarPorId(@PathVariable Long id) {
        Publicacion publicacion = publicacionService.buscarPorId(id);
        return ResponseEntity.ok(new PublicacionResponseDTO(publicacion));
    }

    @PostMapping
    public ResponseEntity<PublicacionResponseDTO> crear(@Valid @RequestBody PublicacionRequestDTO dto) {
        Publicacion creada = publicacionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PublicacionResponseDTO(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicacionResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody PublicacionRequestDTO dto) {
        Publicacion actualizada = publicacionService.actualizar(id, dto);
        return ResponseEntity.ok(new PublicacionResponseDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        publicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

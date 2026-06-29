package tuti.desi.hogarya.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.hogarya.dtos.InteresadoRequestDTO;
import tuti.desi.hogarya.entidades.Interesado;
import tuti.desi.hogarya.repositorios.InteresadoRepository;

@Service
public class InteresadoService {

    private final InteresadoRepository interesadoRepository;

    public InteresadoService(InteresadoRepository interesadoRepository) {
        this.interesadoRepository = interesadoRepository;
    }

    @Transactional(readOnly = true)
    public List<Interesado> listarTodos() {
        return interesadoRepository.findByEliminadoFalse();
    }

    @Transactional(readOnly = true)
    public Interesado buscarPorId(Long id) {
        Interesado interesado = interesadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un interesado con id " + id));
        if (Boolean.TRUE.equals(interesado.getEliminado())) {
            throw new IllegalArgumentException("El interesado con id " + id + " fue eliminado");
        }
        return interesado;
    }

    @Transactional
    public Interesado crear(InteresadoRequestDTO dto) {
        Interesado interesado = new Interesado();
        interesado.setNombre(dto.getNombre());
        interesado.setApellido(dto.getApellido());
        interesado.setContacto(dto.getContacto());
        return interesadoRepository.save(interesado);
    }

    @Transactional
    public Interesado actualizar(Long id, InteresadoRequestDTO dto) {
        Interesado interesado = buscarPorId(id);
        interesado.setNombre(dto.getNombre());
        interesado.setApellido(dto.getApellido());
        interesado.setContacto(dto.getContacto());
        return interesadoRepository.save(interesado);
    }

    @Transactional
    public void eliminar(Long id) {
        Interesado interesado = buscarPorId(id);
        interesado.setEliminado(true);
        interesadoRepository.save(interesado);
    }
}

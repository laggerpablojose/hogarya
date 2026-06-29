package tuti.desi.hogarya.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.InteresVisita;

@Repository
public interface InteresVisitaRepository extends JpaRepository<InteresVisita, Long> {

    List<InteresVisita> findByEliminadoFalse();

    List<InteresVisita> findByPropiedad_IdAndEliminadoFalse(Long propiedadId);

    List<InteresVisita> findByInteresado_IdAndEliminadoFalse(Long interesadoId);
}

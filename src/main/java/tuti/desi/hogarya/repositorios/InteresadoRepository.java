package tuti.desi.hogarya.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.Interesado;

@Repository
public interface InteresadoRepository extends JpaRepository<Interesado, Long> {

    List<Interesado> findByEliminadoFalse();
}

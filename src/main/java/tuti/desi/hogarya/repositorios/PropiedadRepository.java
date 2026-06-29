package tuti.desi.hogarya.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.Propiedad;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadoFalse();

    List<Propiedad> findByEstadoAndEliminadoFalse(EstadoPropiedad estado);

    List<Propiedad> findByCiudadIgnoreCaseAndEliminadoFalse(String ciudad);
}

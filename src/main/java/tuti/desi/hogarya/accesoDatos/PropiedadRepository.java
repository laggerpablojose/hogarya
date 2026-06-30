package tuti.desi.hogarya.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.entidades.TipoPropiedad;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadoFalse();

    List<Propiedad> findByEliminadoFalseAndEstado(EstadoPropiedad estado);

    List<Propiedad> findByEliminadoFalseAndTipo(TipoPropiedad tipo);

    List<Propiedad> findByEliminadoFalseAndCiudadContainingIgnoreCase(String ciudad);

    List<Propiedad> findByEliminadoFalseAndDireccionContainingIgnoreCase(String direccion);

    Optional<Propiedad> findByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadoFalse(
            String direccion, String ciudad);
}

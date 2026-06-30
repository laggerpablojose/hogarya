package tuti.desi.hogarya.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.HistorialEstadoPropiedad;

@Repository
public interface HistorialEstadoPropiedadRepository extends JpaRepository<HistorialEstadoPropiedad, Long> {
}

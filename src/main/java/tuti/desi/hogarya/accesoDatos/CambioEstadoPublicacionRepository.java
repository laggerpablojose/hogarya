package tuti.desi.hogarya.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.CambioEstadoPublicacion;

@Repository
public interface CambioEstadoPublicacionRepository extends JpaRepository<CambioEstadoPublicacion, Long> {
}
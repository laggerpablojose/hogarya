package tuti.desi.hogarya.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.EstadoPublicacion;
import tuti.desi.hogarya.entidades.Publicacion;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByEliminadoFalse();

    List<Publicacion> findByEstadoPublicacionAndEliminadoFalse(EstadoPublicacion estadoPublicacion);

    List<Publicacion> findByPropiedad_IdAndEliminadoFalse(Long propiedadId);

    List<Publicacion> findByPropiedad_CiudadIgnoreCaseAndEliminadoFalse(String ciudad);

    Optional<Publicacion> findByPropiedad_IdAndEstadoPublicacionAndEliminadoFalse(Long propiedadId,
            EstadoPublicacion estadoPublicacion);

    @Query("""
            SELECT p FROM Publicacion p
            WHERE p.eliminado = false
              AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId)
              AND (:ciudad IS NULL OR LOWER(p.propiedad.ciudad) = LOWER(:ciudad))
              AND (:estado IS NULL OR p.estadoPublicacion = :estado)
              AND (:precioMin IS NULL OR p.precioMensualAlquiler >= :precioMin)
              AND (:precioMax IS NULL OR p.precioMensualAlquiler <= :precioMax)
            ORDER BY p.id DESC
            """)
    List<Publicacion> buscarConFiltros(
            @Param("propiedadId") Long propiedadId,
            @Param("ciudad") String ciudad,
            @Param("estado") EstadoPublicacion estado,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);
}

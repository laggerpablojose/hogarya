package tuti.desi.hogarya.accesoDatos;

import java.util.List;

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

    List<Publicacion> findByPropiedad_IdAndEstadoPublicacionAndEliminadoFalse(
            Long propiedadId,
            EstadoPublicacion estadoPublicacion
    );

    @Query("""
            SELECT p
            FROM Publicacion p
            WHERE p.eliminado = false
              AND (:estadoPublicacion IS NULL OR p.estadoPublicacion = :estadoPublicacion)
              AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId)
              AND (:ciudad IS NULL OR LOWER(p.propiedad.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
            """)
    List<Publicacion> buscarConFiltros(
            @Param("estadoPublicacion") EstadoPublicacion estadoPublicacion,
            @Param("propiedadId") Long propiedadId,
            @Param("ciudad") String ciudad
    );
}
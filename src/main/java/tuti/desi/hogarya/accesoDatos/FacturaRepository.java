package tuti.desi.hogarya.accesoDatos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.EstadoFactura;
import tuti.desi.hogarya.entidades.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByEliminadoFalse();

    List<Factura> findByContratoIdAndContratoEliminadoFalseAndEliminadoFalse(Long contratoId);

    List<Factura> findByEstadoAndEliminadoFalse(EstadoFactura estado);

    boolean existsByContratoIdAndEliminadoFalseAndFechaEmisionLessThanEqualAndFechaVencimientoGreaterThanEqual(
            Long contratoId,
            LocalDate fechaFin,
            LocalDate fechaInicio
    );
}
package tuti.desi.hogarya.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.HistorialEstadoFactura;

@Repository
public interface HistorialEstadoFacturaRepository extends JpaRepository<HistorialEstadoFactura, Long> {
	
	List<HistorialEstadoFactura> findByFacturaIdOrderByFechaDesc(Long facturaId);
}

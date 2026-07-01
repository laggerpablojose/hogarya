package tuti.desi.hogarya.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.Contrato;
import tuti.desi.hogarya.entidades.EstadoContrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

    Optional<Contrato> findByIdAndEliminadoFalse(Long id);
}

package com.hogarya.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {

    boolean existsByFacturaId(Long facturaId);

    @Query("select p.id from Pago p where p.factura.id = :facturaId")
    Optional<Long> findIdByFacturaId(@Param("facturaId") Long facturaId);
}

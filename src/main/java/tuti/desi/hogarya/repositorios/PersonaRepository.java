package tuti.desi.hogarya.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.hogarya.entidades.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    List<Persona> findByEliminadoFalse();
}

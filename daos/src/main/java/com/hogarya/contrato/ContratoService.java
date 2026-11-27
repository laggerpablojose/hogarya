package com.hogarya.contrato;

import com.hogarya.common.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository repository;

    @Transactional(readOnly = true)
    public ContratoResponse obtener(Long id) {
        Contrato contrato = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Contrato", id));
        return ContratoResponse.de(contrato);
    }
}

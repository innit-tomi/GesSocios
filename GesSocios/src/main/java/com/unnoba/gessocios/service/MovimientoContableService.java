package com.unnoba.gessocios.service;

import com.unnoba.gessocios.model.MovimientoContable;
import com.unnoba.gessocios.repository.MovimientoContableRep;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoContableService {

    private final MovimientoContableRep repository;

    public MovimientoContableService(MovimientoContableRep repository) {
        this.repository = repository;
    }

    public void registrarMovimiento(MovimientoContable movimiento) {
        repository.save(movimiento);
    }

    public List<MovimientoContable> listarTodos() {
        return repository.findAll();
    }
}

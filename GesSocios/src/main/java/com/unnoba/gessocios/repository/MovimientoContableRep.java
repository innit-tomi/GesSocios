package com.unnoba.gessocios.repository;

import com.unnoba.gessocios.model.MovimientoContable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovimientoContableRep extends JpaRepository<MovimientoContable, Long> {

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoContable m WHERE m.tipo = 'ENTRADA'")
    double obtenerTotalEntradas();

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoContable m WHERE m.tipo = 'SALIDA'")
    double obtenerTotalSalidas();
}

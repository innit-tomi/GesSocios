package com.unnoba.gessocios.repository;

import com.unnoba.gessocios.model.Cuota;
import com.unnoba.gessocios.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRep extends JpaRepository<Cuota, Long> {

    Cuota findTopBySocioOrderByFechaPagoDesc(Socio socio);

    List<Cuota> findBySocio(Socio socio);

}

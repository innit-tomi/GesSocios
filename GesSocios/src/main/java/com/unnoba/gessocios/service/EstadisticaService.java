package com.unnoba.gessocios.service;

import com.unnoba.gessocios.repository.MovimientoContableRep;
import com.unnoba.gessocios.repository.SocioRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EstadisticaService {

    private final MovimientoContableRep movimientoRep;
    private final SocioRep socioRep;

    @Autowired
    public EstadisticaService(MovimientoContableRep movimientoRep, SocioRep socioRep) {
        this.movimientoRep = movimientoRep;
        this.socioRep = socioRep;
    }

    public double obtenerBalanceIngresos() {
        double entradas = movimientoRep.obtenerTotalEntradas();
        double salidas = movimientoRep.obtenerTotalSalidas();
        return entradas - salidas;
    }

    public Map<String, Long> obtenerEstadisticasSocios() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", socioRep.contarTotalSocios());
        stats.put("femeninos", socioRep.contarFemeninos());
        stats.put("masculinos", socioRep.contarMasculinos());
        stats.put("otro", socioRep.contarOtroGenero());
        stats.put("activos", socioRep.contarActivos());
        stats.put("inactivos", socioRep.contarInactivos());
        stats.put("morosos", socioRep.contarMorosos());
        return stats;
    }
}

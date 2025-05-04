package com.unnoba.gessocios.service;

import com.unnoba.gessocios.model.Cuota;
import com.unnoba.gessocios.model.Socio;
import com.unnoba.gessocios.repository.CuotaRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuotaService {

    @Autowired
    private CuotaRep cuotaRepository;

    public Cuota guardarCuota(Cuota cuota) {
        return cuotaRepository.save(cuota);
    }

    public List<Cuota> obtenerCuotasPorSocio(Socio socio) {
        return cuotaRepository.findBySocio(socio);
    }
}

package com.unnoba.gessocios.service;

import com.unnoba.gessocios.model.Pago;
import com.unnoba.gessocios.model.Socio;
import com.unnoba.gessocios.repository.PagoRep;
import com.unnoba.gessocios.repository.SocioRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioService {

    private final SocioRep socioRepository;
    private final PagoRep pagoRepository;

    @Autowired
    public SocioService(SocioRep socioRepository, PagoRep pagoRepository) {
        this.socioRepository = socioRepository;
        this.pagoRepository = pagoRepository;
    }

    public List<Socio> listarTodos() {
        return socioRepository.findAll();
    }

    public Socio crear(Socio socio) {
        return socioRepository.save(socio);
    }

    public Socio buscarPorId(Long id) {
        return socioRepository.findById(id).orElse(null);
    }

    public Socio actualizar(Long id, Socio socio) {
        socio.setId(id);
        return socioRepository.save(socio);
    }

    public void eliminar(Long id) {
        socioRepository.deleteById(id);
    }

    public List<Socio> buscarPorApellido(String apellido) {
        return socioRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    public Pago registrarPago(Long socioId) {
        Socio socio = buscarPorId(socioId);
        if (socio == null) {
            throw new RuntimeException("Socio no encontrado");
        }
        Pago pago = new Pago(socio);
        return pagoRepository.save(pago);
    }
}
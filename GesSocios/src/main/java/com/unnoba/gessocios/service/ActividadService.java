package com.unnoba.gessocios.service;

import com.unnoba.gessocios.model.Actividad;
import com.unnoba.gessocios.repository.ActividadRep;
import com.unnoba.gessocios.repository.SocioRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {

    private final ActividadRep actividadRep;

    @Autowired
    private SocioRep socioActividadRepository;  // Este es tu SocioRep

    public ActividadService(ActividadRep actividadRepository) {
        this.actividadRep = actividadRepository;
    }

    public List<Actividad> obtenerTodas() {
        return actividadRep.findAll();
    }

    public Actividad obtenerPorId(Long id) {
        return actividadRep.findById(id).orElse(null);
    }

    public void guardar(Actividad actividad) {
        actividadRep.save(actividad);
    }

    public void eliminarActividad(Long id) throws IllegalStateException {
        // Verifica si la actividad tiene socios inscritos
        long cantidadInscriptos = socioActividadRepository.countByActividadId(id);

        if (cantidadInscriptos > 0) {
            throw new IllegalStateException("No se puede eliminar la actividad: hay " + cantidadInscriptos + " socios inscritos.");
        }

        // Si no hay socios, se puede eliminar
        actividadRep.deleteById(id);
    }
}

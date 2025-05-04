package com.unnoba.gessocios.repository;

import com.unnoba.gessocios.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRep extends JpaRepository<Actividad, Long> {
}

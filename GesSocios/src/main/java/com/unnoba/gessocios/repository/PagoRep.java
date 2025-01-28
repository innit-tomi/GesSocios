package com.unnoba.gessocios.repository;

import com.unnoba.gessocios.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRep extends JpaRepository<Pago, Long> {
}
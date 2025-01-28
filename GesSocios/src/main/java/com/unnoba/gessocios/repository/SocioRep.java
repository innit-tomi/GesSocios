package com.unnoba.gessocios.repository;

import com.unnoba.gessocios.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocioRep extends JpaRepository<Socio, Long> {
    @Query("SELECT s FROM Socio s WHERE LOWER(s.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Socio> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);
}
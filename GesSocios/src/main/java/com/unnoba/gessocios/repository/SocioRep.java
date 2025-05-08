package com.unnoba.gessocios.repository;

import com.unnoba.gessocios.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRep extends JpaRepository<Socio, Long> {

    // Buscar por parte del apellido (ignora mayúsculas/minúsculas)
    @Query("SELECT s FROM Socio s WHERE LOWER(s.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Socio> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);

    // Buscar por número de socio (único) - Devuelve Optional por seguridad
    Optional<Socio> findByNumeroSocio(Integer numeroSocio);

    // Buscar por nombre (parcial, ignora mayúsculas/minúsculas)
    Socio findByNombreContainingIgnoreCase(String nombre);

    // Contar los socios asociados a una actividad específica
    @Query("SELECT COUNT(s) FROM Socio s JOIN s.actividades a WHERE a.id = :actividadId")
    long countByActividadId(Long actividadId);

    Optional<Socio> findByDocumento(String documento);


    // Totales
    @Query("SELECT COUNT(s) FROM Socio s")
    long contarTotalSocios();

    // Por género
    @Query("SELECT COUNT(s) FROM Socio s WHERE s.genero = 'FEMENINO'")
    long contarFemeninos();

    @Query("SELECT COUNT(s) FROM Socio s WHERE s.genero = 'MASCULINO'")
    long contarMasculinos();

    @Query("SELECT COUNT(s) FROM Socio s WHERE s.genero = 'OTRO'")
    long contarOtroGenero();

    // Por estado
    @Query("SELECT COUNT(s) FROM Socio s WHERE s.estado = 'ACTIVO'")
    long contarActivos();

    @Query("SELECT COUNT(s) FROM Socio s WHERE s.estado = 'INACTIVO'")
    long contarInactivos();

    // Morosos (los que no pagaron este mes)
    @Query("SELECT COUNT(s) FROM Socio s WHERE s.estado = 'MOROSO'")
    long contarMorosos();


}

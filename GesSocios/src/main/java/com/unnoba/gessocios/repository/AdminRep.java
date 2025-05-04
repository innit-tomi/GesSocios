package com.unnoba.gessocios.repository;

import com.unnoba.gessocios.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRep extends JpaRepository<Admin, String> {
    Admin findByNombre(String nombre);
}

package com.unnoba.gessocios.model;

import jakarta.persistence.*;

@Entity
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private Double precioSocio;
    private Double precioNoSocio;
    private Double precioJubilado;
    private Double precioNoSocioJubilado;
    private String profesor;

    public Actividad() {}

    public Actividad(String nombre, Categoria categoria, Double precioSocio, Double precioNoSocio, Double precioJubilado, Double precioNoSocioJubilado, String profesor) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioSocio = precioSocio;
        this.precioNoSocio = precioNoSocio;
        this.precioJubilado = precioJubilado;
        this.precioNoSocioJubilado = precioNoSocioJubilado;
        this.profesor = profesor;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Double getPrecioSocio() { return precioSocio; }
    public void setPrecioSocio(Double precioSocio) { this.precioSocio = precioSocio; }

    public Double getPrecioNoSocio() { return precioNoSocio; }
    public void setPrecioNoSocio(Double precioNoSocio) { this.precioNoSocio = precioNoSocio; }


    public Double getPrecioJubilado() {
        return precioJubilado;
    }

    public void setPrecioJubilado(Double precioJubilado) {
        this.precioJubilado = precioJubilado;
    }

    public Double getPrecioNoSocioJubilado() {
        return precioNoSocioJubilado;
    }

    public void setPrecioNoSocioJubilado(Double precioNoSocioJubilado) {
        this.precioNoSocioJubilado = precioNoSocioJubilado;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}

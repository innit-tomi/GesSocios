package com.unnoba.gessocios.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    private LocalDate fechaPago;
    private double montoTotal;

    @Column
    private String metodoPago;

    // Constructor sin argumentos para JPA
    public Cuota() {}

    // Constructor que recibe socio y montoTotal
    public Cuota(Socio socio, double montoTotal) {
        this.socio = socio;
        this.fechaPago = LocalDate.now();
        this.montoTotal = montoTotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    // Calcula el próximo pago sumando 1 mes a la fecha de pago
    public LocalDate getProximoPago() {
        return fechaPago.plusMonths(1);
    }

    // Calcula los días restantes desde hoy hasta el próximo pago
    public long calcularDiasRestantes() {
        LocalDate now = LocalDate.now();
        if(now.isAfter(getProximoPago())) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(now, getProximoPago());
    }


    public String getMetodoPago(){
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago){
        this.metodoPago = metodoPago;
    }

}

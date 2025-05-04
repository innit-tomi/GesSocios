package com.unnoba.gessocios.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer getNumeroSocio() {
        return numeroSocio;
    }

    public void setNumeroSocio(Integer numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    @Column(unique = true)
    private Integer numeroSocio; // Número aleatorio

    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String documento;
    private String domicilio;
    private String tipo;
    private String localidad;
    private String mail;
    @Enumerated(EnumType.STRING)
    private EstadoSocio estado;
    @Transient
    private String cantActividades;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCantActividades() {
        return cantActividades;
    }

    public void setCantActividades(String cantActividades) {
        this.cantActividades = cantActividades;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    @ManyToMany
    @JoinTable(
            name = "socio_actividad",
            joinColumns = @JoinColumn(name = "socio_id"),
            inverseJoinColumns = @JoinColumn(name = "actividad_id")
    )
    private List<Actividad> actividades = new ArrayList<>();

    public List<Actividad> getActividades() { return actividades; }
    public void setActividades(List<Actividad> actividades) { this.actividades = actividades; }

    public void agregarActividad(Actividad actividad) {
        this.actividades.add(actividad);
    }

    public void removerActividad(Actividad actividad) {
        this.actividades.remove(actividad);
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }



    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuota> cuotas;

    // Campos transitorios para la lógica de pagos
    @Transient
    private boolean pagoEsteMes;

    @Transient
    private long diasRestantes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public EstadoSocio getEstado() { return estado; }
    public void setEstado(EstadoSocio estado) { this.estado = estado; }

    public Genero getGenero() { return genero; }

    public void setGenero(Genero genero) { this.genero = genero;}

    public boolean isPagoEsteMes() {
        return pagoEsteMes;
    }
    public void setPagoEsteMes(boolean pagoEsteMes) {
        this.pagoEsteMes = pagoEsteMes;
    }
    public long getDiasRestantes() {
        return diasRestantes;
    }
    public void setDiasRestantes(long diasRestantes) {
        this.diasRestantes = diasRestantes;
    }
}

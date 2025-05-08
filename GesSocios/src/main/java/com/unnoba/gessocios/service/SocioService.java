package com.unnoba.gessocios.service;

import com.unnoba.gessocios.model.*;
import com.unnoba.gessocios.repository.CuotaRep;
import com.unnoba.gessocios.repository.SocioRep;
import com.unnoba.gessocios.service.MovimientoContableService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class SocioService {


    private final SocioRep socioRepository;
    private final CuotaRep cuotaRepository;


    @Autowired
    private MovimientoContableService movimientoContableService;

    @Autowired
    public SocioService(SocioRep socioRepository, CuotaRep cuotaRepository) {
        this.socioRepository = socioRepository;
        this.cuotaRepository = cuotaRepository;
    }

    @Autowired
    private ActividadService actividadService;

    public List<Socio> listarTodos() {
        return socioRepository.findAll();
    }

    private Random random = new Random();

    public Socio crearSocio(Socio socio) {
        // Verificar si ya existe un socio con ese número de documento
        Optional<Socio> existente = socioRepository.findByDocumento(socio.getDocumento());

        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un socio con el número de documento: " + socio.getDocumento());
        }

        socio.setNumeroSocio(generarNumeroSocioUnico());
        socio.setEstado(EstadoSocio.INACTIVO);
        return socioRepository.save(socio);
    }

    private Integer generarNumeroSocioUnico() {
        int numero;
        do {
            numero = random.nextInt(90000) + 10000; // genera entre 10000 y 99999
        } while (socioRepository.findByNumeroSocio(numero).isPresent());
        return numero;
    }

    public Socio buscarPorId(Long id) {
        return socioRepository.findById(id).orElse(null);
    }

    public Socio actualizar(Long id, Socio socioActualizado) {
        Socio socioExistente = socioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Socio no encontrado"));

        socioExistente.setNombre(socioActualizado.getNombre());
        socioExistente.setApellido(socioActualizado.getApellido());
        socioExistente.setDocumento(socioActualizado.getDocumento());
        socioExistente.setLocalidad(socioActualizado.getLocalidad());
        socioExistente.setDomicilio(socioActualizado.getDomicilio());
        socioExistente.setTipo(socioActualizado.getTipo());
        socioExistente.setMail(socioActualizado.getMail());
        socioExistente.setGenero(socioActualizado.getGenero());
        // NO TOCAR: número de socio ni estado
        return socioRepository.save(socioExistente);
    }
    public void guardar(Socio socio) {
        socioRepository.save(socio);  // Usamos save() para persistir el objeto Socio
    }

    public void eliminar(Long id) {
        socioRepository.deleteById(id);
    }

    public List<Socio> buscarPorApellido(String apellido) {
        return socioRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    @Transactional
    public Cuota registrarCobro(Long socioId, String metodoPago) {
        Socio socio = buscarPorId(socioId);
        if (socio == null) {
            throw new RuntimeException("Socio no encontrado");
        }

        // NO permitir pago si ya está ACTIVO
        if (socio.getEstado() == EstadoSocio.ACTIVO) {
            System.out.println("Socio ya está activo: " + socio.getId());
            throw new RuntimeException("El socio ya está activo y no necesita pagar otra cuota.");
        }

        // Si pasa la validación, registramos la cuota
        double montoCuota = calcularMontoCuota(socio);
        Cuota nuevaCuota = new Cuota(socio, montoCuota);
        nuevaCuota.setMetodoPago(metodoPago);
        nuevaCuota = cuotaRepository.save(nuevaCuota);

        // Actualizar estado del socio
        socio.setEstado(EstadoSocio.ACTIVO);
        socio.setPagoEsteMes(true);
        socio.setDiasRestantes(nuevaCuota.calcularDiasRestantes());
        socioRepository.save(socio);

        // Registrar movimiento contable
        MovimientoContable movimiento = new MovimientoContable();
        movimiento.setFecha(LocalDate.now());
        movimiento.setMonto(montoCuota); // Ahora usamos montoCuota
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setMotivo("Cuota de socio " + socio.getNumeroSocio());
        movimiento.setSocio(socio);
        movimientoContableService.registrarMovimiento(movimiento);

        System.out.println("Cuota registrada correctamente para el socio: " + socio.getId());
        return nuevaCuota;
    }

    public List<Socio> listarSocios() {
        List<Socio> socios = socioRepository.findAll();
        for (Socio socio : socios) {
            Cuota ultimoCobro = cuotaRepository.findTopBySocioOrderByFechaPagoDesc(socio);
            if (ultimoCobro != null) {
                socio.setDiasRestantes(ultimoCobro.calcularDiasRestantes());
                socio.setPagoEsteMes(ultimoCobro.getProximoPago().isAfter(LocalDate.now()));
            } else {
                socio.setDiasRestantes(0L); // Nunca ha pagado
                socio.setPagoEsteMes(false);
            }
        }
        return socios;
    }

    // Mtodo auxiliar para calcular el monto de la cuota
// Mtodo auxiliar para calcular el monto de la cuota
    private double calcularMontoCuota(Socio socio) {
        double monto = 0;

        // Si es vitalicio, no paga nada
        if ("Vitalicio".equalsIgnoreCase(socio.getTipo())) {
            return 0;
        }

        // Sumar actividades
        for (Actividad act : socio.getActividades()) {
            monto += act.getPrecioSocio(); // Asumiendo que Actividad tiene getPrecioSocio()
        }

        // Cuota fija según tipo
        switch (socio.getTipo()) {
            case "Jubilado":
                monto += 1000;
                break;
            case "Adulto":
                monto += 2000;
                break;
            case "Menor":
                monto += 500;
                break;
            default:
                monto += 0; // cualquier otro tipo no suma nada extra
        }

        return monto;
    }

    @Transactional
    public void agregarActividad(Long socioId, Long actividadId) {
        Socio socio = socioRepository.findById(socioId).orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        Actividad actividad = actividadService.obtenerPorId(actividadId);

        if (!socio.getActividades().contains(actividad)) {
            socio.agregarActividad(actividad);
            socioRepository.save(socio);
        }
    }

    @Transactional
    public void quitarActividad(Long socioId, Long actividadId) {
        Socio socio = socioRepository.findById(socioId).orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        Actividad actividad = actividadService.obtenerPorId(actividadId);

        if (socio.getActividades().contains(actividad)) {
            socio.removerActividad(actividad);
            socioRepository.save(socio);
        }
    }

    public Socio buscarPorNombreONumero(String termino) {
        try {
            Integer numero = Integer.parseInt(termino);
            return socioRepository.findByNumeroSocio(numero).orElse(null);
        } catch (NumberFormatException e) {
            return socioRepository.findByNombreContainingIgnoreCase(termino);
        }
    }

    public List<Cuota> listarTodasLasCuotas() {
        return cuotaRepository.findAll();
    }

    @Transactional
    public void verificarYActualizarEstado(Long socioId) {
        Socio socio = buscarPorId(socioId);
        if (socio == null) return;

        Cuota ultimaCuota = cuotaRepository.findTopBySocioOrderByFechaPagoDesc(socio);

        if (ultimaCuota != null) {
            YearMonth mesUltimaCuota = YearMonth.from(ultimaCuota.getFechaPago());
            YearMonth mesActual = YearMonth.now();

            if (mesUltimaCuota.isBefore(mesActual)) {
                socio.setEstado(EstadoSocio.MOROSO);
                socioRepository.save(socio);
                System.out.println("Socio " + socio.getId() + " actualizado a MOROSO.");
            }
        }
    }

    public void actualizarEstadosDeSocios() {
        List<Socio> socios = socioRepository.findAll();

        for (Socio socio : socios) {
            if (socio.getEstado() == EstadoSocio.INACTIVO) {
                continue; // No tocamos inactivos
            }

            Cuota ultimaCuota = cuotaRepository.findTopBySocioOrderByFechaPagoDesc(socio);

            if (ultimaCuota != null && ultimaCuota.getFechaPago() != null) {
                LocalDate fechaPago = ultimaCuota.getFechaPago();
                if (fechaPago.isAfter(LocalDate.now().minusMonths(1))) {
                    socio.setEstado(EstadoSocio.ACTIVO);
                } else {
                    socio.setEstado(EstadoSocio.MOROSO);
                }
            } else {
                // Nunca pagó -> MOROSO
                socio.setEstado(EstadoSocio.MOROSO);
            }

            socioRepository.save(socio);
        }
    }

    public Optional<Socio> buscarPorDocumento(String documento) {
        return socioRepository.findByDocumento(documento);  // Asegúrate que findByDocumento devuelva un Optional
    }





}

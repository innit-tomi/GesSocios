package com.unnoba.gessocios.controller;

import com.unnoba.gessocios.model.Actividad;
import com.unnoba.gessocios.model.Cuota;
import com.unnoba.gessocios.model.EstadoSocio;
import com.unnoba.gessocios.model.Socio;
import com.unnoba.gessocios.service.CuotaService;
import com.unnoba.gessocios.service.SocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/cuotas")
public class CuotaController {

    @Autowired
    private SocioService socioService;

    @Autowired
    private CuotaService cuotaService;

    @GetMapping("/buscar")
    public String buscarFormulario() {
        return "cuotas/buscar";
    }

    @PostMapping("/buscar")
    public String buscarSocio(@RequestParam String termino, Model model) {
        Socio socio = socioService.buscarPorNombreONumero(termino);
        if (socio == null) {
            model.addAttribute("mensaje", "Socio no encontrado");
            return "cuotas/buscar";
        }
        model.addAttribute("socio", socio);
        model.addAttribute("montoTotal", calcularMontoTotal(socio));
        return "cuotas/pagar";
    }

    @PostMapping("/pagar/{id}")
    public String pagarCuota(@PathVariable Long id, @RequestParam String metodoPago) {
        try {
            socioService.registrarCobro(id, metodoPago);
        } catch (RuntimeException e) {
            // Opcional: puedes registrar el error o mostrar un mensaje al usuario
            System.out.println("Error al registrar el cobro: " + e.getMessage());
            // Redirigir a una página de error o mostrar un mensaje en la misma página
            return "redirect:/cuotas/buscar?error=" + e.getMessage();
        }
        return "redirect:/cuotas/buscar?success";
    }



    private double calcularMontoTotal(Socio socio) {
        double monto = 0;

        // Si es vitalicio, no paga nada
        if ("Vitalicio".equalsIgnoreCase(socio.getTipo())) {
            return 0;
        }

        // Sumar actividades
        for (Actividad act : socio.getActividades()) {
            monto += act.getPrecioSocio(); // asumiendo getter
        }

        // Cuota fija según tipo
        switch (socio.getTipo()) {
            case "Jubilado": monto += 1000; break;
            case "Adulto": monto += 2000; break;
            case "Menor": monto += 500; break;
            default: monto += 0; // cualquier otro tipo no suma nada extra
        }

        return monto;
    }


    @GetMapping("/lista")
    public String listarCuotas(Model model) {
        List<Cuota> cuotas = socioService.listarTodasLasCuotas();
        model.addAttribute("cuotas", cuotas);
        return "cuotas/lista";
    }

}

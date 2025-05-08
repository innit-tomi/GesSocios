package com.unnoba.gessocios.controller;

import com.unnoba.gessocios.service.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ESTA ES LA CORRECTA
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @Autowired
    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping
    public String mostrarEstadisticas(Model model) {
        double balance = estadisticaService.obtenerBalanceIngresos();
        Map<String, Long> statsSocios = estadisticaService.obtenerEstadisticasSocios();

        model.addAttribute("balance", balance);
        model.addAttribute("statsSocios", statsSocios);

        return "estadisticas/estadistica"; // estadisticas.html
    }
}

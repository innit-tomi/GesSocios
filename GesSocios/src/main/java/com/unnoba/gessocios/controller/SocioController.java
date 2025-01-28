package com.unnoba.gessocios.controller;

import com.unnoba.gessocios.model.Socio;
import com.unnoba.gessocios.service.SocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/socios")
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("socios", socioService.listarTodos());
        return "socios/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("socio", new Socio());
        return "socios/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute Socio socio) {
        socioService.crear(socio);
        return "redirect:/socios";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Socio socio = socioService.buscarPorId(id);
        if (socio == null) {
            return "redirect:/socios"; // Manejo de error si no se encuentra el socio
        }
        model.addAttribute("socio", socio);
        return "socios/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Socio socio) {
        socioService.actualizar(id, socio);
        return "redirect:/socios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        socioService.eliminar(id);
        return "redirect:/socios";
    }

    @GetMapping("/buscar")
    public String buscarPorApellido(@RequestParam(required = false) String apellido, Model model) {
        List<Socio> socios;
        if (apellido != null && !apellido.isEmpty()) {
            socios = socioService.buscarPorApellido(apellido);
        } else {
            socios = socioService.listarTodos(); // Si no hay apellido, muestra todos
        }
        model.addAttribute("socios", socios);
        model.addAttribute("apellido", apellido); // Para mantener el valor en el formulario
        return "socios/lista";
    }

    @PostMapping("/{id}/pagar")
    public String registrarPago(@PathVariable Long id) {
        socioService.registrarPago(id);
        return "redirect:/socios";
    }
}
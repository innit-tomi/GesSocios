package com.unnoba.gessocios.controller;

import com.unnoba.gessocios.model.Actividad;
import com.unnoba.gessocios.model.Categoria;
import com.unnoba.gessocios.service.ActividadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping
    public String listarActividades(Model model) {
        model.addAttribute("actividades", actividadService.obtenerTodas());
        return "actividades/lista";
    }

    @GetMapping("/nueva")
    public String nuevaActividad(Model model) {
        model.addAttribute("actividad", new Actividad());
        model.addAttribute("categorias", Arrays.asList(Categoria.values()));
        return "actividades/formulario";
    }

    @PostMapping("/guardar")
    public String guardarActividad(@ModelAttribute Actividad actividad) {
        actividadService.guardar(actividad);
        return "redirect:/actividades";
    }

    @GetMapping("/editar/{id}")
    public String editarActividad(@PathVariable Long id, Model model) {
        Actividad actividad = actividadService.obtenerPorId(id);
        if (actividad != null) {
            model.addAttribute("actividad", actividad);
            model.addAttribute("categorias", Arrays.asList(Categoria.values()));
            return "actividades/editar";
        }
        return "redirect:/actividades";
    }

    @PostMapping("/actualizar")
    public String actualizarActividad(@ModelAttribute Actividad actividad) {
        actividadService.guardar(actividad);
        return "redirect:/actividades";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarActividad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            actividadService.eliminarActividad(id);
            redirectAttributes.addFlashAttribute("successMessage", "Actividad eliminada correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/actividades";
    }
}

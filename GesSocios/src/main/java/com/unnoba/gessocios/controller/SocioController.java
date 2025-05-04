package com.unnoba.gessocios.controller;

import com.unnoba.gessocios.model.Actividad;
import com.unnoba.gessocios.model.EstadoSocio;
import com.unnoba.gessocios.model.Socio;
import com.unnoba.gessocios.service.ActividadService;
import com.unnoba.gessocios.service.SocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/socios")
public class SocioController {

    private final SocioService socioService;

    @Autowired
    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    // Se utiliza el mtodo listarSocios() del servicio, que ya asigna
    // los días restantes y el estado de pago
    @GetMapping
    public String listar(Model model) {
        List<Socio> socios = socioService.listarSocios();
        for (Socio socio : socios) {
            socio.setCantActividades(String.valueOf(socio.getActividades().size()));
        }
        model.addAttribute("socios", socios);
        return "socios/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("socio", new Socio());
        return "socios/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute Socio socio) {
        socioService.crearSocio(socio);
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
            socios = socioService.listarSocios();
        }
        model.addAttribute("socios", socios);
        model.addAttribute("apellido", apellido); // Para mantener el valor en el formulario
        return "socios/lista";
    }


    @PostMapping("/darDeBaja/{id}")
    public String darDeBaja(@PathVariable Long id) {
        Socio socio = socioService.buscarPorId(id);
        if (socio != null) {
            socio.setEstado(EstadoSocio.INACTIVO);
            socioService.guardar(socio);
        }
        return "redirect:/socios";
    }



    @Autowired
    private ActividadService actividadService;

    // Mostrar detalles del socio con actividades disponibles
    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Long id, Model model) {
        Socio socio = socioService.buscarPorId(id);
        if (socio == null) return "redirect:/socios";

        List<Actividad> actividadesDisponibles = actividadService.obtenerTodas();
        model.addAttribute("socio", socio);
        model.addAttribute("actividades", actividadesDisponibles);
        return "socios/detalles";
    }

    // Agregar actividad a un socio
    @PostMapping("/{id}/agregarActividad")
    public String agregarActividad(@PathVariable Long id, @RequestParam Long actividadId) {
        socioService.agregarActividad(id, actividadId);
        return "redirect:/socios/detalles/" + id;
    }

    // Quitar actividad de un socio
    @PostMapping("/{id}/quitarActividad/{actividadId}")
    public String quitarActividad(@PathVariable Long id, @PathVariable Long actividadId) {
        socioService.quitarActividad(id, actividadId);
        return "redirect:/socios/detalles/" + id;
    }

    @PostMapping("/verificar-estados")
    public String verificarEstados() {
        socioService.actualizarEstadosDeSocios();
        return "redirect:/socios";
    }


}

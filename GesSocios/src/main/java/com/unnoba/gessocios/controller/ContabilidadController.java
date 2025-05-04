    package com.unnoba.gessocios.controller;

    import com.unnoba.gessocios.model.MovimientoContable;
    import com.unnoba.gessocios.model.TipoMovimiento;
    import com.unnoba.gessocios.service.MovimientoContableService;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;

    @Controller
    @RequestMapping("/contabilidad")
    public class ContabilidadController {

        private final MovimientoContableService service;

        public ContabilidadController(MovimientoContableService service) {
            this.service = service;
        }

        @GetMapping
        public String listar(Model model) {
            model.addAttribute("movimientos", service.listarTodos());
            return "MovimientosContables/lista"; // 👈 ahora coincide con tu carpeta
        }

        @GetMapping("/nuevo")
        public String nuevoForm(Model model) {
            model.addAttribute("movimiento", new MovimientoContable());
            model.addAttribute("tipos", TipoMovimiento.values());
            return "MovimientosContables/formulario"; // 👈 ahora coincide con tu carpeta
        }

        @PostMapping
        public String guardar(@ModelAttribute MovimientoContable movimiento) {
            movimiento.setFecha(LocalDate.now());
            service.registrarMovimiento(movimiento);
            return "redirect:/contabilidad";
        }
    }

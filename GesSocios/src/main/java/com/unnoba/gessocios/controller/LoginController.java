package com.unnoba.gessocios.controller;

import com.unnoba.gessocios.model.Admin;
import com.unnoba.gessocios.repository.AdminRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private AdminRep adminRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login/login"; // Carga templates/login/login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String nombre,
                        @RequestParam String contrasena,
                        RedirectAttributes redirectAttributes) {

        Admin admin = adminRepository.findByNombre(nombre);

        if (admin != null && admin.getContrasena().equals(contrasena)) {
            return "redirect:/"; // Si es correcto, redirige a la página principal
        } else {
            redirectAttributes.addFlashAttribute("error", "Nombre o contraseña incorrectos.");
            return "redirect:/login"; // Redirige de nuevo al formulario
        }
    }
}

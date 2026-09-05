package com.reciclaje.proyectodeaula.controller;

import com.reciclaje.proyectodeaula.model.PuntoReciclaje;
import com.reciclaje.proyectodeaula.model.Solicitud;
import com.reciclaje.proyectodeaula.repository.PuntoReciclajeRepository;
import com.reciclaje.proyectodeaula.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CiudadanoController {

    @Autowired
    private PuntoReciclajeRepository puntoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @GetMapping("/ciudadano")
    public String dashboard(Model model, Authentication authentication) {

        List<PuntoReciclaje> puntos = puntoRepository.findAll();

        List<Solicitud> historial =
                solicitudRepository.findByCiudadanoNombre(authentication.getName());

        model.addAttribute("puntos", puntos);

        model.addAttribute("historial", historial);

        return "ciudadano";
    }

    @PostMapping("/crear-solicitud")
    public String crearSolicitud(
            @RequestParam String descripcion,
            @RequestParam String direccionMaps,
            Authentication authentication
    ) {

        Solicitud s = new Solicitud();

        s.setDescripcion(descripcion);

        s.setDireccionMaps(direccionMaps);

        s.setEstado("PENDIENTE");

        s.setFechaSolicitud(LocalDateTime.now());

        s.setCiudadanoNombre(authentication.getName());

        solicitudRepository.save(s);

        return "redirect:/ciudadano";
    }
}

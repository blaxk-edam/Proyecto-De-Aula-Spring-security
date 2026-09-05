package com.reciclaje.proyectodeaula.controller;

import com.reciclaje.proyectodeaula.model.Solicitud;
import com.reciclaje.proyectodeaula.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecicladorController {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @GetMapping("/reciclador")
    public String reciclador(Model model) {

        model.addAttribute("solicitudes", solicitudRepository.findAll());

        return "reciclador";
    }

    @GetMapping("/completar/{id}")
    public String completar(@PathVariable Long id) {

        Solicitud s = solicitudRepository.findById(id).orElse(null);

        if(s != null){

            s.setEstado("RECOGIDO");

            solicitudRepository.save(s);
        }

        return "redirect:/reciclador";
    }
}
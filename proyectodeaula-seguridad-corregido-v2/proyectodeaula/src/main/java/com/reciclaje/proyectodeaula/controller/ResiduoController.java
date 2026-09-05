package com.reciclaje.proyectodeaula.controller;

import com.reciclaje.proyectodeaula.model.Residuo;
import com.reciclaje.proyectodeaula.repository.ResiduoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residuos")
@CrossOrigin("*")
public class ResiduoController {

    private final ResiduoRepository repository;

    public ResiduoController(ResiduoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Residuo> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Residuo guardar(@RequestBody Residuo residuo) {
        return repository.save(residuo);
    }

    @PutMapping("/recoger/{id}")
    public Residuo recoger(@PathVariable Long id,
                           @RequestParam String reciclador) {

        Residuo r = repository.findById(id).orElse(null);

        if (r != null) {

            if (!"EJEMPLO".equals(r.getEstado())) {

                r.setEstado("RECOGIDO");
                r.setRecogidoPor(reciclador);

                repository.save(r);
            }
        }

        return r;
    }

    @PutMapping("/clasificar/{id}")
    public Residuo clasificar(@PathVariable Long id,
                              @RequestParam String acopio) {

        Residuo r = repository.findById(id).orElse(null);

        if (r != null) {

            r.setEstado("CLASIFICADO");
            r.setClasificadoPor(acopio);

            repository.save(r);
        }

        return r;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
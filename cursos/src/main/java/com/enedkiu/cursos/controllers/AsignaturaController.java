package com.enedkiu.cursos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.enedkiu.cursos.services.AsignaturaService;
import com.enedkiu.cursos.models.AsignaturaModel;



@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {
    @Autowired
    AsignaturaService asignaturaService;

    @GetMapping()
    public Iterable<AsignaturaModel> getAllAsignaturas() {
        return asignaturaService.getAllAsignaturas();
    }

    @GetMapping("/{asignaturaId}")
    public AsignaturaModel getOneAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.getOneAsignatura(asignaturaId);
    }

    @PostMapping()
    public AsignaturaModel saveAsignatura(@RequestBody AsignaturaModel asignatura) {
        return asignaturaService.saveAsignatura(asignatura);
    }

    @PutMapping("/{asignaturaId}")
    public AsignaturaModel updateAsignatura(@PathVariable Long asignaturaId, @RequestBody AsignaturaModel asignatura) {
        return asignaturaService.updateAsignatura(asignaturaId, asignatura);
    }
    
    @DeleteMapping("/{asignaturaId}")
    public void deleteAsignatura(@PathVariable Long asignaturaId) {
        asignaturaService.deleteAsignatura(asignaturaId);
    }
    
}

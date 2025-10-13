package com.enedkiu.cursos.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.enedkiu.cursos.models.NotaModel;
import com.enedkiu.cursos.services.NotaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/notas")
public class NotaController {
    @Autowired
    NotaService notaService;

    @GetMapping("/{notaId}")
    public NotaModel getOneNota(@PathVariable Long notaId) {
        return notaService.getOneNota(notaId);
    }

    @PostMapping("")
    public NotaModel saveNota(@RequestBody NotaModel nota) {
        return notaService.saveNota(nota);
    }

    @PutMapping("/{notaId}")
    public NotaModel updateNota(@PathVariable Long notaId, @RequestBody NotaModel nota) {
        return notaService.updateNota(notaId, nota);
    }

    @DeleteMapping("/{notaId}")
    public void deleteNota(@PathVariable Long notaId) {
        notaService.deleteNota(notaId);
    }
    

    // Obtener las notas de un estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public Iterable<NotaModel> getNotasbyEstudianteId(@PathVariable Long estudianteId) {
        return notaService.getNotasByEstudianteId(estudianteId);
    }
    
    
}

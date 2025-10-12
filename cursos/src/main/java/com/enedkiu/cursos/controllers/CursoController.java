package com.enedkiu.cursos.controllers;

import com.enedkiu.cursos.models.CursoModel;
import com.enedkiu.cursos.services.CursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired 
    CursoService cursoService;

    @GetMapping()
    public Iterable<CursoModel> getAllCursos() {
        return cursoService.getAllCursos();
    }

    @GetMapping("/{CursoId}")
    public CursoModel getOneCurso(@PathVariable Long CursoId) {
        return cursoService.getOneCurso(CursoId);
    }

    @PostMapping()
    public CursoModel saveCurso(@RequestBody CursoModel Curso) {
        return cursoService.saveCurso(Curso);
    }

    @PutMapping("/{CursoId}")
    public CursoModel updateCurso(@PathVariable Long CursoId, @RequestBody CursoModel Curso) {
        return cursoService.updateCurso(CursoId, Curso);
    }
    
    @DeleteMapping("/{CursoId}")
    public void deleteCurso(@PathVariable Long CursoId) {
        cursoService.deleteCurso(CursoId);
    }
}

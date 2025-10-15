package com.enedkiu.cursos.controllers;

import com.enedkiu.cursos.dto.CursoUpdateDTO;
import com.enedkiu.cursos.models.CursoModel;
import com.enedkiu.cursos.models.TareaModel;
import com.enedkiu.cursos.services.CursoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired 
    CursoService cursoService;

    // --- API RESTful del Modelo
    @GetMapping()
    public Iterable<CursoModel> getAllCursos() {
        return cursoService.getAllCursos();
    }

    @GetMapping("/{cursoId}")
    public CursoModel getOneCurso(@PathVariable Long cursoId) {
        return cursoService.getOneCurso(cursoId);
    }

    @PostMapping()
    public CursoModel saveCurso(@RequestBody CursoModel Curso) {
        return cursoService.saveCurso(Curso);
    }

    @PutMapping("/{cursoId}")
    public CursoModel updateCurso(@PathVariable Long cursoId, @RequestBody CursoUpdateDTO cursoDto) {
        return cursoService.updateCurso(cursoId, cursoDto);
    }
    
    @DeleteMapping("/{cursoId}")
    public void deleteCurso(@PathVariable Long cursoId) {
        cursoService.deleteCurso(cursoId);
    }

    // --- Tareas por curso ---

    @GetMapping("/{cursoId}/tareas")
    public List<TareaModel> getTareas(@PathVariable Long cursoId) {
        return cursoService.getOneCurso(cursoId).getTareas();
    }

    // --- Filtrar cursos por campos ---

    @GetMapping("/estudiante/{estudianteId}")
    public Iterable<CursoModel> getCursosByEstudianteId(@PathVariable Long estudianteId) {
        return cursoService.getCursosByEstudianteId(estudianteId);
    }

    @GetMapping("/profesor/{profesorId}")
    public Iterable<CursoModel> getCursosByProfesorId(@PathVariable Long profesorId) {
        return cursoService.getCursosByProfesorId(profesorId);
    }
}

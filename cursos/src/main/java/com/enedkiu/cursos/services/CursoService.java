package com.enedkiu.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.*;
import org.springframework.web.server.ResponseStatusException;

import com.enedkiu.cursos.dto.CursoUpdateDTO;
import com.enedkiu.cursos.models.AsignaturaModel;
import com.enedkiu.cursos.models.CursoModel;
import com.enedkiu.cursos.repositories.AsignaturaRepository;
import com.enedkiu.cursos.repositories.CursoRepository;
import com.enedkiu.cursos.repositories.TareaRepository;

import jakarta.transaction.Transactional;

@Service
public class CursoService {
    @Autowired
    CursoRepository cursoRepository;

    @Autowired 
    AsignaturaRepository asignaturaRepository;

    @Autowired
    TareaRepository tareaRepository;

    public Iterable<CursoModel> getAllCursos() {
        return cursoRepository.findAll();
    }

    public Iterable<CursoModel> getCursosByEstudianteId(Long estudianteId) {
        return cursoRepository.findByEstudiantesIdContaining(estudianteId);
    }

    public Iterable<CursoModel> getCursosByProfesorId(Long profesorId) {
        return cursoRepository.findByProfesorId(profesorId);
    }

    public CursoModel getOneCurso(Long cursoId) {
        return cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public CursoModel saveCurso(CursoModel curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    public CursoModel updateCurso(Long cursoId, CursoUpdateDTO cursoDto) {
        // 1. Obtener el Curso existente (debe estar dentro de la transacción)
        CursoModel curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + cursoId));

        // 2. Actualizar campos simples
        if (cursoDto.getNombre() != null) {
            curso.setNombre(cursoDto.getNombre());
        }
        if (cursoDto.getProfesorId() != null) {
            curso.setProfesorId(cursoDto.getProfesorId());
        }
        if (cursoDto.getEstudiantesId() != null) {
            curso.setEstudiantesId(cursoDto.getEstudiantesId());
        }

        // 3. Manejar la relación Uno-a-Uno (Asignatura)
        if (cursoDto.getAsignaturaId() != null) {
            AsignaturaModel nuevaAsignatura = asignaturaRepository.findById(cursoDto.getAsignaturaId())
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con ID: " + cursoDto.getAsignaturaId()));
            curso.setAsignatura(nuevaAsignatura); 
        }

        return cursoRepository.save(curso);
    }

    public void deleteCurso(Long cursoId) {
        cursoRepository.deleteById(cursoId);
    }
}

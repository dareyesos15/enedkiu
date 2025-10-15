package com.enedkiu.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.*;
import org.springframework.web.server.ResponseStatusException;

import com.enedkiu.cursos.dto.CursoSaveDTO;
import com.enedkiu.cursos.dto.CursoUpdateDTO;
import com.enedkiu.cursos.models.AsignaturaModel;
import com.enedkiu.cursos.models.CursoModel;
import com.enedkiu.cursos.repositories.AsignaturaRepository;
import com.enedkiu.cursos.repositories.CursoRepository;

import jakarta.transaction.Transactional;

@Service
public class CursoService {
    @Autowired
    CursoRepository cursoRepository;

    @Autowired 
    AsignaturaRepository asignaturaRepository;

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

    @Transactional
    public CursoModel saveCurso(CursoSaveDTO cursoDTO) {
        CursoModel curso = new CursoModel();

        curso.setNombre(cursoDTO.getNombre());
        curso.setProfesorId(cursoDTO.getProfesorId());
        curso.setEstudiantesId(cursoDTO.getEstudiantesId());

        AsignaturaModel nuevaAsignatura = asignaturaRepository.findById(cursoDTO.getAsignaturaId())
            .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con ID: " + cursoDTO.getAsignaturaId()));
        curso.setAsignatura(nuevaAsignatura); 

        return cursoRepository.save(curso);
    }

    @Transactional
    public CursoModel updateCurso(Long cursoId, CursoUpdateDTO dto) {
        // 1. Obtener el Curso existente (debe estar dentro de la transacción)
        CursoModel curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + cursoId));

        // 2. Actualizar campos simples
        if (dto.getNombre() != null) {
            curso.setNombre(dto.getNombre());
        }
        if (dto.getProfesorId() != null) {
            curso.setProfesorId(dto.getProfesorId());
        }
        if (dto.getEstudiantesId() != null) {
            curso.setEstudiantesId(dto.getEstudiantesId());
        }

        // 3. Manejar la relación Uno-a-Uno (Asignatura)
        if (dto.getAsignaturaId() != null) {
            AsignaturaModel nuevaAsignatura = asignaturaRepository.findById(dto.getAsignaturaId())
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con ID: " + dto.getAsignaturaId()));
            curso.setAsignatura(nuevaAsignatura); 
        }

        return cursoRepository.save(curso);
    }

    public void deleteCurso(Long cursoId) {
        cursoRepository.deleteById(cursoId);
    }
}

package com.enedkiu.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.*;
import org.springframework.web.server.ResponseStatusException;

import com.enedkiu.cursos.models.CursoModel;
import com.enedkiu.cursos.repositories.CursoRepository;

@Service
public class CursoService {
    @Autowired
    CursoRepository cursoRepository;

    public Iterable<CursoModel> getAllCursos() {
        return cursoRepository.findAll();
    }

    public CursoModel getOneCurso(Long cursoId) {
        return cursoRepository.findById(cursoId)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public CursoModel saveCurso(CursoModel curso) {
        return cursoRepository.save(curso);
    }

    public CursoModel updateCurso(Long cursoId, CursoModel newCurso) {
        return cursoRepository
        .findById(cursoId).map(curso -> {

            curso.setProfesor_id(newCurso.getProfesor_id());

            return cursoRepository.save(curso);
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteCurso(Long cursoId) {
        cursoRepository.deleteById(cursoId);
    }
}

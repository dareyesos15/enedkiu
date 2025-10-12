package com.enedkiu.cursos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enedkiu.cursos.models.AsignaturaModel;
import com.enedkiu.cursos.repositories.AsignaturaRepository;

@Service
public class AsignaturaService {
    @Autowired
    AsignaturaRepository asignaturaRepository;

    public ArrayList<AsignaturaModel> getAllAsignaturas() {
        return (ArrayList<AsignaturaModel>) asignaturaRepository.findAll();
    }

    public AsignaturaModel getOneAsignatura(Long asignaturaId) {
        return asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public AsignaturaModel saveAsignatura(AsignaturaModel asignatura) {
        return asignaturaRepository.save(asignatura);
    }

    public AsignaturaModel updateAsginatura(Long asignaturaId, AsignaturaModel newAsginatura) {
        return asignaturaRepository
        .findById(asignaturaId).map(asignatura -> {

            asignatura.setNombre(newAsginatura.getNombre());
            asignatura.setDepartamento(newAsginatura.getDepartamento());
            asignatura.setDescripcion(newAsginatura.getDescripcion());

            return asignaturaRepository.save(asignatura);
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteAsignatura(Long asignaturaId) {
        asignaturaRepository.deleteById(asignaturaId);
    }
}

package com.enedkiu.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enedkiu.cursos.models.TareaModel;
import com.enedkiu.cursos.repositories.TareaRepository;

@Service
public class TareaService {
    @Autowired
    TareaRepository tareaRepository;

    public Iterable<TareaModel> getAllTareas() {
        return tareaRepository.findAll();
    }

    public TareaModel getOneTarea(Long tareaId) {
        return tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public TareaModel saveTarea(TareaModel tarea) {
        return tareaRepository.save(tarea);
    }

    public TareaModel updateTarea(Long tareaId, TareaModel newTarea) {
        return tareaRepository
                .findById(tareaId).map(tarea -> {

                    tarea.setTitulo(newTarea.getTitulo());
                    tarea.setDescripcion(newTarea.getDescripcion());
                    tarea.setNota(newTarea.getNota());
                    tarea.setEntregado(newTarea.isEntregado());

                    return tareaRepository.save(tarea);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteTarea(Long tareaId) {
        tareaRepository.deleteById(tareaId);
    }
}

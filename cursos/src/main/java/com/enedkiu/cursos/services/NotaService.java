package com.enedkiu.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enedkiu.cursos.models.NotaModel;
import com.enedkiu.cursos.repositories.NotaRepository;

@Service
public class NotaService {
    @Autowired
    NotaRepository notaRepository;

    public NotaModel getOneNota(Long notaId) {
        return notaRepository.findById(notaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public NotaModel saveNota(NotaModel nota) {
        return notaRepository.save((nota));
    }

    public NotaModel updateNota(Long notaId, NotaModel newNota) {
        return notaRepository
        .findById(notaId).map(nota -> {

            nota.setMensaje(newNota.getMensaje());
            nota.setCalificacion(newNota.getCalificacion());

            return notaRepository.save(nota);
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteNota(Long notaId) {
        notaRepository.deleteById(notaId);
    }

    public Iterable<NotaModel> getNotasByEstudianteId(Long estudianteId) {
        return notaRepository.findByEstudianteId(estudianteId);
    }
}

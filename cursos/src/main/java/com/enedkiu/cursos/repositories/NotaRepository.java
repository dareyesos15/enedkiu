package com.enedkiu.cursos.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enedkiu.cursos.models.NotaModel;

@Repository
public interface NotaRepository extends CrudRepository<NotaModel, Long> {
    public abstract Iterable<NotaModel> findByEstudianteId(Long estudianteId);
} 
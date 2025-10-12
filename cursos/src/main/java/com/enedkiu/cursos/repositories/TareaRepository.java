package com.enedkiu.cursos.repositories;

import org.springframework.stereotype.Repository;

import org.springframework.data.repository.CrudRepository;
import com.enedkiu.cursos.models.TareaModel;

@Repository
public interface TareaRepository extends CrudRepository<TareaModel, Long>{
    
}

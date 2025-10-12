package com.enedkiu.cursos.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.enedkiu.cursos.models.CursoModel;

@Repository
public interface CursoRepository extends CrudRepository<CursoModel, Long>{
    
}

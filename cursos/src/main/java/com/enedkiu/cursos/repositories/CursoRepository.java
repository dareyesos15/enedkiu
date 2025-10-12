package com.enedkiu.cursos.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.enedkiu.cursos.models.CursoModel;

@Repository
public interface CursoRepository extends CrudRepository<CursoModel, Long> {
    //Para buscar en una colección de datos, se debe utilizar Containing al final de la función
    public abstract Iterable<CursoModel> findByEstudiantesIdContaining(Long estudianteId);

    public abstract Iterable<CursoModel> findByProfesorId(Long profesorId);
}

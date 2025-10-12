package com.enedkiu.cursos.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.enedkiu.cursos.models.AsignaturaModel;

@Repository
public interface AsignaturaRepository extends CrudRepository<AsignaturaModel, Long>{
    //Busca registros en la base de datos según un campo (GROUP BY)
    public abstract ArrayList<AsignaturaModel> findByDepartamento(String departamento); 
}

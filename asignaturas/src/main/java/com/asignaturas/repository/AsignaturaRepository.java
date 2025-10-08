package com.asignaturas.repository;

import com.asignaturas.entity.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    List<Asignatura> findByNombreContainingIgnoreCase(String nombre);
    List<Asignatura> findByFacultad(String facultad);
    List<Asignatura> findByProfesorId(Long profesorId);  // Nuevo método
    
    @Query("SELECT ae.asignatura FROM AsignaturaEstudiante ae WHERE ae.estudianteId = :estudianteId")
    List<Asignatura> findAsignaturasByEstudianteId(@Param("estudianteId") Long estudianteId);
}
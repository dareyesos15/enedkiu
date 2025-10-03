package com.asignaturas.repository;

import com.asignaturas.entity.AsignaturaEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignaturaEstudianteRepository extends JpaRepository<AsignaturaEstudiante, Long> {
    List<AsignaturaEstudiante> findByEstudianteId(Long estudianteId);
    List<AsignaturaEstudiante> findByAsignaturaId(Long asignaturaId);
    boolean existsByEstudianteIdAndAsignaturaId(Long estudianteId, Long asignaturaId);
    void deleteByEstudianteIdAndAsignaturaId(Long estudianteId, Long asignaturaId);
    
    @Query("SELECT COUNT(ae) FROM AsignaturaEstudiante ae WHERE ae.asignatura.id = :asignaturaId")
    Long countEstudiantesByAsignaturaId(@Param("asignaturaId") Long asignaturaId);
}
package com.enedkiu.cursos.dto;

import java.util.List;

/**
 * DTO utilizado para la creación (POST) de un nuevo Curso.
 * Permite recibir el ID de la asignatura necesaria para establecer la relación
 * antes de guardar la entidad.
 */
public class CursoSaveDTO {
    
    // Propiedades del CursoModel
    private String nombre;
    private Long profesorId;
    private List<Long> estudiantesId;

    // Campo específico para manejar la relación (necesario para el Servicio)
    private Long asignaturaId;

    // --- Getters y Setters ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Long profesorId) {
        this.profesorId = profesorId;
    }

    public List<Long> getEstudiantesId() {
        return estudiantesId;
    }

    public void setEstudiantesId(List<Long> estudiantesId) {
        this.estudiantesId = estudiantesId;
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }
}

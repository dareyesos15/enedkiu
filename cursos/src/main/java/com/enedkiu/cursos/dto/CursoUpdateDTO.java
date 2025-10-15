package com.enedkiu.cursos.dto;

import java.util.List;

/**
 * DTO utilizado para recibir datos de un Curso al realizar una operación PUT o PATCH.
 * Solo incluye los campos que se pueden actualizar.
 */
public class CursoUpdateDTO {
    
    // Campos de la entidad CursoModel
    private String nombre;
    private Long profesorId;
    private List<Long> estudiantesId;

    // Relación Uno-a-Uno: Asignatura (se usa solo el ID)
    private Long asignaturaId; 

    public CursoUpdateDTO() {}

    // --- Getters y Setters ---

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public List<Long> getEstudiantesId() { return estudiantesId; }
    public void setEstudiantesId(List<Long> estudiantesId) { this.estudiantesId = estudiantesId; }

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }
}

package com.asignaturas.dto;

import com.asignaturas.entity.AsignaturaEstudiante;

public class AsignaturaEstudianteDTO {
    private Long id;
    private Long estudianteId;
    private Long asignaturaId;
    private String asignaturaNombre;
    
    // Constructor desde Entity
    public AsignaturaEstudianteDTO(AsignaturaEstudiante entity) {
        this.id = entity.getId();
        this.estudianteId = entity.getEstudianteId();
        this.asignaturaId = entity.getAsignatura().getId();
        this.asignaturaNombre = entity.getAsignatura().getNombre();
    }
    
    // Constructor vacío
    public AsignaturaEstudianteDTO() {}
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    
    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }
    
    public String getAsignaturaNombre() { return asignaturaNombre; }
    public void setAsignaturaNombre(String asignaturaNombre) { this.asignaturaNombre = asignaturaNombre; }
}
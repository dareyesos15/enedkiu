package com.asignaturas.dto;

public class InscripcionRequestDTO {
    private Long estudianteId;
    private Long asignaturaId;
    
    // Constructores
    public InscripcionRequestDTO() {}
    
    public InscripcionRequestDTO(Long estudianteId, Long asignaturaId) {
        this.estudianteId = estudianteId;
        this.asignaturaId = asignaturaId;
    }
    
    // Getters y Setters
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    
    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }
}

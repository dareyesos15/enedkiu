package com.asignaturas.dto;

import com.asignaturas.entity.Asignatura;

public class AsignaturaDTO {
    private Long id;
    private String nombre;
    private String facultad;
    private Long profesorId;
    private String grupo;
    
    // Constructor desde Entity
    public AsignaturaDTO(Asignatura asignatura) {
        this.id = asignatura.getId();
        this.nombre = asignatura.getNombre();
        this.facultad = asignatura.getFacultad();
        this.profesorId = asignatura.getProfesorId();
        this.grupo = asignatura.getGrupo();
    }
    
    // Constructor para crear nuevas asignaturas
    public AsignaturaDTO() {}
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }
    
    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }
    
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
}
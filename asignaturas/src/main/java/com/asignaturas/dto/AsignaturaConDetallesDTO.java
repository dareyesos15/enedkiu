package com.asignaturas.dto;

import java.util.List;

public class AsignaturaConDetallesDTO {
    private Long id;
    private String nombre;
    private String facultad;
    private Long profesorId;
    private String grupo;
    private UsuarioDTO profesor;
    private List<UsuarioDTO> estudiantes;
    private Integer totalEstudiantes;
    
    // Constructores
    public AsignaturaConDetallesDTO() {}
    
    public AsignaturaConDetallesDTO(Long id, String nombre, String facultad, Long profesorId, String grupo) {
        this.id = id;
        this.nombre = nombre;
        this.facultad = facultad;
        this.profesorId = profesorId;
        this.grupo = grupo;
    }
    
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
    
    public UsuarioDTO getProfesor() { return profesor; }
    public void setProfesor(UsuarioDTO profesor) { this.profesor = profesor; }
    
    public List<UsuarioDTO> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(List<UsuarioDTO> estudiantes) { this.estudiantes = estudiantes; }
    
    public Integer getTotalEstudiantes() { return totalEstudiantes; }
    public void setTotalEstudiantes(Integer totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }
}
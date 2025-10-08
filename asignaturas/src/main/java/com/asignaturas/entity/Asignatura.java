package com.asignaturas.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asignaturas")
public class Asignatura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "facultad", nullable = false, length = 100)
    private String facultad;
    
    @Column(name = "profesor_id", nullable = false)
    private Long profesorId;  
    
    @Column(name = "grupo", nullable = false, length = 50)
    private String grupo;
    
    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AsignaturaEstudiante> estudiantes = new ArrayList<>();
    
    // Constructores
    public Asignatura() {}
    
    public Asignatura(String nombre, String facultad, Long profesorId, String grupo) {
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
    
    public List<AsignaturaEstudiante> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(List<AsignaturaEstudiante> estudiantes) { this.estudiantes = estudiantes; }
}
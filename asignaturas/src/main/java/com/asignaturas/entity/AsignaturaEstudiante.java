package com.asignaturas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "asignatura_estudiantes")
public class AsignaturaEstudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura;
    
    // Constructores, Getters y Setters
    public AsignaturaEstudiante() {}
    
    public AsignaturaEstudiante(Long estudianteId, Asignatura asignatura) {
        this.estudianteId = estudianteId;
        this.asignatura = asignatura;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    
    public Asignatura getAsignatura() { return asignatura; }
    public void setAsignatura(Asignatura asignatura) { this.asignatura = asignatura; }
}

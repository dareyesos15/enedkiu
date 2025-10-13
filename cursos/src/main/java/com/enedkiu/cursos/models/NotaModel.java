package com.enedkiu.cursos.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "notas")
public class NotaModel {

    // --- Campos ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    private float calificacion;
    private String mensaje;

    @Column(name = "estudiante_id")
    private Long estudianteId;

    // Relacion con la tabla tareas
    @ManyToOne
    @JoinColumn(name = "tarea_id", nullable = false)
    @JsonBackReference
    private TareaModel tarea;

    // --- Metodos getter y setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float nota) {
        this.calificacion = nota;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public TareaModel getTarea() {
        return tarea;
    }

    public void setTarea(TareaModel tarea) {
        this.tarea = tarea;
    }

}

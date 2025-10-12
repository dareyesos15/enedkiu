package com.enedkiu.cursos.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity // especifica que la clase es una entidad
@Table(name = "curso") // nombre que tendrá en la base de datos
public class CursoModel {

    // --- Campos ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    // crear nueva tabla para la relacion estudiante-curso
    @ElementCollection
    @CollectionTable(name = "estudiante_curso", joinColumns = @JoinColumn(name = "curso_id"))
    @Column(name = "estudiante_id")
    private List<Long> estudiantesId;

    private Long profesorId;

    // relación con la tabla asignaturas
    @ManyToOne
    @JoinColumn(name = "asignatura_id", nullable = false)
    @JsonBackReference
    private AsignaturaModel asignatura; // Asignatura a la que pertenece el curso

    // relacion con la tabla tareas
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TareaModel> tareas;

    // --- metodos getter y setter ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Long profesor_id) {
        this.profesorId = profesor_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Long> getEstudiantesId() {
        return estudiantesId;
    }

    public void setEstudiantesId(List<Long> estudiantes_id) {
        this.estudiantesId = estudiantes_id;
    }

    public AsignaturaModel getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(AsignaturaModel asignatura) {
        this.asignatura = asignatura;
    }

    public List<TareaModel> getTareas() {
        return tareas;
    }

    public void setTareas(List<TareaModel> tareas) {
        this.tareas = tareas;
    }

}

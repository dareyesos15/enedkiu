package com.enedkiu.cursos.models;

import java.util.List;
import jakarta.persistence.*;

@Entity // especifica que la clase es una entidad
@Table(name = "curso") // nombre que tendrá en la base de datos
public class CursoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Long profesor_id;
    private String nombre;

    //crear nueva tabla para la relacion estudiante-curso
    @ElementCollection
    @CollectionTable(name = "estudiante_curso", joinColumns = @JoinColumn(name = "curso_id"))
    @Column(name = "estudiante_id")
    private List<Long> estudiantes_id;

    // relación con la tabla asignaturas
    @ManyToOne
    @JoinColumn(name = "asignatura_id", nullable = false)
    private AsignaturaModel asignatura; // Asignatura a la que pertenece el curso

    //metodos getter y setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfesor_id() {
        return profesor_id;
    }

    public void setProfesor_id(Long profesor_id) {
        this.profesor_id = profesor_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Long> getEstudiantes_id() {
        return estudiantes_id;
    }

    public void setEstudiantes_id(List<Long> estudiantes_id) {
        this.estudiantes_id = estudiantes_id;
    }

    public AsignaturaModel getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(AsignaturaModel asignatura) {
        this.asignatura = asignatura;
    }

    // metodos getter y setter
    

}

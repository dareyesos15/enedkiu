package com.enedkiu.cursos.models;

import java.util.ArrayList;
import jakarta.persistence.*;

@Entity //especifica que la clase es una entidad
@Table(name = "usuario") //nombre que tendrá en la base de datos
public class CursoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Long asignatura_id; //Asignatura a la que pertenece el curso
    private Long profesor_id;
    private ArrayList<Long> estudiantes_id;

    //metodos getter y setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getAsignatura_id() {
        return asignatura_id;
    }
    public void setAsignatura_id(Long asignatura_id) {
        this.asignatura_id = asignatura_id;
    }
    public Long getProfesor_id() {
        return profesor_id;
    }
    public void setProfesor_id(Long profesor_id) {
        this.profesor_id = profesor_id;
    }
    public ArrayList<Long> getEstudiantes_id() {
        return estudiantes_id;
    }
    public void setEstudiantes_id(ArrayList<Long> estudiantes_id) {
        this.estudiantes_id = estudiantes_id;
    }
    
    
}

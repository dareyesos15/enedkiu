package com.enedkiu.cursos.models;

import java.util.List;
import jakarta.persistence.*;

@Entity // especifica que la clase es una entidad
@Table(name = "asignatura") // nombre que tendrá en la base de datos
public class AsignaturaModel {
    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String nombre;
    private String departamento;
    private String descripcion;

    //Relacion con la tabla cursos
    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CursoModel> cursos;

    //constructores
    public AsignaturaModel() {

    }

    public AsignaturaModel(String nombre, String departamento, String descripcion) {
        this.nombre = nombre;
        this.departamento = departamento;
        this.descripcion = descripcion;
    }

    // metodos getter y setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}

package com.enedkiu.cursos.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.enedkiu.cursos.models.AsignaturaModel;
import com.enedkiu.cursos.models.CursoModel;
import com.enedkiu.cursos.repositories.AsignaturaRepository;
import com.enedkiu.cursos.repositories.CursoRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cargar datos solo si las tablas están vacías
        if (asignaturaRepository.count() == 0) {
            loadAsignaturaData();
        }
        if (cursoRepository.count() == 0) {
            loadCursoData();
        }
    }

    private void loadAsignaturaData() {
        List<AsignaturaModel> asignaturas = Arrays.asList(
                new AsignaturaModel("Cálculo Diferencial", "Ciencias Básicas", "Curso introductorio al cálculo."),
                new AsignaturaModel("Programación Orientada a Objetos", "Sistemas y Computación",
                        "Paradigmas de la POO con Java."),
                new AsignaturaModel("Bases de Datos Relacionales", "Sistemas y Computación",
                        "Diseño y gestión de bases de datos con SQL."),
                new AsignaturaModel("Física Mecánica", "Ciencias Básicas", "Estudio del movimiento y las fuerzas."));
        asignaturaRepository.saveAll(asignaturas);
        System.out.println("Datos de Asignaturas cargados.");
    }

    private void loadCursoData() {
        List<AsignaturaModel> asignaturas = (List<AsignaturaModel>) asignaturaRepository.findAll();
        if (asignaturas.size() < 4) {
            System.out.println("No hay suficientes asignaturas para crear los cursos.");
            return;
        }

        // IDs de profesores solicitados
        long[] profesorIds = { 2, 5, 6, 7, 5 };

        // Asignaturas para cada curso
        AsignaturaModel[] cursoAsignaturas = {
                asignaturas.get(0), // Curso 1 -> Asignatura 1
                asignaturas.get(1), // Curso 2 -> Asignatura 2
                asignaturas.get(2), // Curso 3 -> Asignatura 3
                asignaturas.get(3), // Curso 4 -> Asignatura 4
                asignaturas.get(1) // Curso 5 -> Asignatura 2 (adicional)
        };

        List<CursoModel> cursosACrear = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CursoModel curso = new CursoModel();
            curso.setAsignatura(cursoAsignaturas[i]);
            curso.setProfesor_id(profesorIds[i]);
            curso.setEstudiantes_id(generateRandomStudentIds());

            String nombre = String.format("GRUPO %d-%d", i+1, curso.getAsignatura().getId());
            curso.setNombre(nombre);
            cursosACrear.add(curso);
        }

        cursoRepository.saveAll(cursosACrear);
        System.out.println("Datos de Cursos cargados.");
    }

    private ArrayList<Long> generateRandomStudentIds() {
        // Pool de IDs de estudiantes disponibles
        List<Long> studentPool = new ArrayList<>(
                Arrays.asList(3L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L));
        Collections.shuffle(studentPool); // Mezclar la lista para aleatoriedad

        // Asignar un número aleatorio de estudiantes a cada curso (entre 2 y 5)
        int numberOfStudents = ThreadLocalRandom.current().nextInt(2, 6);

        // Tomar una sublista para el curso actual
        List<Long> assignedStudents = studentPool.subList(0, Math.min(numberOfStudents, studentPool.size()));

        return new ArrayList<>(assignedStudents);
    }
}

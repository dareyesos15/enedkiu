package com.enedkiu.cursos.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enedkiu.cursos.models.*;
import com.enedkiu.cursos.repositories.*;

@Component
public class DataInitializer {

    // Repositorios inyectados
    @Autowired private AsignaturaRepository asignaturaRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private TareaRepository tareaRepository;
    @Autowired private NotaRepository notaRepository;

    // Generador de IDs de estudiantes
    private ArrayList<Long> generateRandomStudentIds() {
        List<Long> studentPool = new ArrayList<>(
                Arrays.asList(3L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L));
        Collections.shuffle(studentPool);
        int numberOfStudents = ThreadLocalRandom.current().nextInt(2, 6);
        List<Long> assignedStudents = studentPool.subList(0, Math.min(numberOfStudents, studentPool.size()));
        return new ArrayList<>(assignedStudents);
    }
    
    // Este método SÍ será interceptado por Spring al ser llamado desde otra clase (DataLoader).
    // Esto garantiza que la sesión de la base de datos esté abierta.
    @Transactional
    public void initializeData() {
        if (asignaturaRepository.count() == 0) {
            loadAsignaturaData();
        }
        if (cursoRepository.count() == 0) {
            loadCursoData();
        }
        if (tareaRepository.count() == 0) {
            loadTareaData();
        }
        if (notaRepository.count() == 0) {
            loadNotaData();
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
        // findAll() se ejecuta dentro de la transacción activa
        List<AsignaturaModel> asignaturas = (List<AsignaturaModel>) asignaturaRepository.findAll();
        if (asignaturas.size() < 4) {
            System.out.println("No hay suficientes asignaturas para crear los cursos.");
            return;
        }

        long[] profesorIds = { 2, 5, 6, 7, 5 };
        AsignaturaModel[] cursoAsignaturas = {
                asignaturas.get(0), asignaturas.get(1), asignaturas.get(2), asignaturas.get(3), asignaturas.get(1)
        };

        List<CursoModel> cursosACrear = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CursoModel curso = new CursoModel();
            curso.setAsignatura(cursoAsignaturas[i]);
            curso.setProfesorId(profesorIds[i]);
            curso.setEstudiantesId(generateRandomStudentIds());

            String nombre = String.format("GRUPO %d-%d", i + 1, curso.getAsignatura().getId());
            curso.setNombre(nombre);
            cursosACrear.add(curso);
        }

        cursoRepository.saveAll(cursosACrear);
        System.out.println("Datos de Cursos cargados.");
    }

    private void loadTareaData() {
        // findAll() se ejecuta dentro de la transacción activa
        List<CursoModel> cursos = (List<CursoModel>) cursoRepository.findAll();
        if (cursos.isEmpty()) {
            System.out.println("No hay cursos para asignar tareas.");
            return;
        }

        List<TareaModel> tareasACrear = new ArrayList<>();
        // Tarea 1 (Cálculo Diferencial)
        TareaModel tarea1 = new TareaModel();
        tarea1.setCurso(cursos.get(0));
        tarea1.setTitulo("Taller 1: Límites y Continuidad");
        tarea1.setDescripcion("Resolver los ejercicios del capítulo 3 del libro guía.");
        tarea1.setFechaEntrega(LocalDateTime.now().minusDays(10));
        tareasACrear.add(tarea1);

        // Tarea 2 (POO)
        TareaModel tarea2 = new TareaModel();
        tarea2.setCurso(cursos.get(1));
        tarea2.setTitulo("Proyecto 1: Clase 'Vehiculo'");
        tarea2.setDescripcion("Implementar herencia y polimorfismo con una jerarquía de vehículos.");
        tarea2.setFechaEntrega(LocalDateTime.now().plusDays(5));
        tareasACrear.add(tarea2);

        // Tarea 3 (POO)
        TareaModel tarea3 = new TareaModel();
        tarea3.setCurso(cursos.get(1));
        tarea3.setTitulo("Quiz 1: Conceptos POO");
        tarea3.setDescripcion("Cuestionario sobre los pilares de la POO.");
        tarea3.setFechaEntrega(LocalDateTime.now().minusWeeks(1));
        tareasACrear.add(tarea3);

        // Tarea 4 (Bases de Datos)
        TareaModel tarea4 = new TareaModel();
        tarea4.setCurso(cursos.get(2));
        tarea4.setTitulo("Laboratorio: Consultas SQL");
        tarea4.setDescripcion("Realizar 10 consultas complejas sobre el modelo de datos Northwind.");
        tarea4.setFechaEntrega(LocalDateTime.now().minusDays(3));
        tareasACrear.add(tarea4);

        tareaRepository.saveAll(tareasACrear);
        System.out.println("Datos de Tareas cargados.");
    }

    private void loadNotaData() {
        // findAll() se ejecuta dentro de la transacción activa
        List<TareaModel> tareas = (List<TareaModel>) tareaRepository.findAll();
        List<CursoModel> cursos = (List<CursoModel>) cursoRepository.findAll();
        List<NotaModel> notasACrear = new ArrayList<>();

        if (tareas.isEmpty() || cursos.isEmpty()) {
            System.out.println("No hay tareas o cursos para asignar notas.");
            return;
        }

        // Tarea 1 (Curso 1)
        // Se accede a la colección Lazy getEstudiantesId() DENTRO de la sesión activa,
        // evitando el LazyInitializationException.
        List<Long> estudiantesCurso1 = cursos.get(0).getEstudiantesId(); 
        TareaModel tarea1 = tareas.get(0);
        
        if (!estudiantesCurso1.isEmpty()) {
            NotaModel nota1 = new NotaModel();
            nota1.setTarea(tarea1);
            nota1.setEstudianteId(estudiantesCurso1.get(0));
            nota1.setCalificacion(4.5f);
            nota1.setMensaje("Excelente presentación y solución completa de los problemas.");
            notasACrear.add(nota1);

            if (estudiantesCurso1.size() > 1) {
                NotaModel nota2 = new NotaModel();
                nota2.setTarea(tarea1);
                nota2.setEstudianteId(estudiantesCurso1.get(1));
                nota2.setCalificacion(2.8f);
                nota2.setMensaje("Conceptos de continuidad erróneos. Debe repasar el capítulo 2.");
                notasACrear.add(nota2);
            }
        }
        
        // Tarea 3 (Curso 2)
        List<Long> estudiantesCurso2 = cursos.get(1).getEstudiantesId(); 
        TareaModel tarea3 = tareas.get(2);
        
        if (!estudiantesCurso2.isEmpty()) {
            NotaModel nota3 = new NotaModel();
            nota3.setTarea(tarea3);
            nota3.setEstudianteId(estudiantesCurso2.get(0));
            nota3.setCalificacion(3.9f);
            nota3.setMensaje("Conocimiento sólido de la abstracción, pero falló en el concepto de encapsulamiento.");
            notasACrear.add(nota3);
            
            if (estudiantesCurso2.size() > 1) {
                NotaModel nota4 = new NotaModel();
                nota4.setTarea(tarea3);
                nota4.setEstudianteId(estudiantesCurso2.get(1));
                nota4.setCalificacion(5.0f);
                nota4.setMensaje("¡Perfecto! Dominio total de los pilares de POO.");
                notasACrear.add(nota4);
            }
        }

        // Tarea 4 (Curso 3)
        List<Long> estudiantesCurso3 = cursos.get(2).getEstudiantesId(); 
        TareaModel tarea4 = tareas.get(3);
        
        if (!estudiantesCurso3.isEmpty()) {
             NotaModel nota5 = new NotaModel();
            nota5.setTarea(tarea4);
            nota5.setEstudianteId(estudiantesCurso3.get(0));
            nota5.setCalificacion(4.2f);
            nota5.setMensaje("Buen manejo de JOINs, pero se sugiere usar más subconsultas en los ejercicios 8 y 9.");
            notasACrear.add(nota5);
        }

        notaRepository.saveAll(notasACrear);
        System.out.println("Datos de Notas cargados.");
    }
}

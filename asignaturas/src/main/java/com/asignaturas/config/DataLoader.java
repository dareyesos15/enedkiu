package com.asignaturas.config;

import com.asignaturas.entity.Asignatura;
import com.asignaturas.entity.AsignaturaEstudiante;
import com.asignaturas.repository.AsignaturaRepository;
import com.asignaturas.repository.AsignaturaEstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AsignaturaRepository asignaturaRepository;
    
    @Autowired
    private AsignaturaEstudianteRepository asignaturaEstudianteRepository;

    @Override
    public void run(String... args) throws Exception {
        // Limpiar datos existentes (opcional)
        asignaturaEstudianteRepository.deleteAll();
        asignaturaRepository.deleteAll();
        
        // Crear asignaturas de ejemplo
        Asignatura matematicas = new Asignatura("Matemáticas Avanzadas", "Facultad de Ciencias", "Dr. Carlos Ruiz", "G01");
        Asignatura fisica = new Asignatura("Física Cuántica", "Facultad de Ciencias", "Dra. María González", "G02");
        Asignatura programacion = new Asignatura("Programación Java", "Facultad de Ingeniería", "Ing. Pedro Martínez", "G03");
        Asignatura historia = new Asignatura("Historia Universal", "Facultad de Humanidades", "Lic. Ana López", "G04");
        Asignatura economia = new Asignatura("Economía Internacional", "Facultad de Economía", "Mtro. Roberto Díaz", "G05");
        
        // Guardar asignaturas
        matematicas = asignaturaRepository.save(matematicas);
        fisica = asignaturaRepository.save(fisica);
        programacion = asignaturaRepository.save(programacion);
        historia = asignaturaRepository.save(historia);
        economia = asignaturaRepository.save(economia);
        
        // Crear inscripciones de estudiantes
        // Estudiante 101 inscrito en 3 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(101L, matematicas));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(101L, programacion));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(101L, historia));
        
        // Estudiante 102 inscrito en 2 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(102L, fisica));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(102L, economia));
        
        // Estudiante 103 inscrito en 4 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(103L, matematicas));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(103L, fisica));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(103L, programacion));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(103L, economia));
        
        // Estudiante 104 inscrito en 1 asignatura
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(104L, historia));
        
        // Estudiante 105 inscrito en 2 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(105L, programacion));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(105L, economia));
        
        System.out.println("Datos de ejemplo cargados correctamente");
        System.out.println("Asignaturas creadas: " + asignaturaRepository.count());
        System.out.println("Inscripciones creadas: " + asignaturaEstudianteRepository.count());
    }
}
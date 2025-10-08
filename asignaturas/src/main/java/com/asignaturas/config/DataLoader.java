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
        Asignatura matematicas = new Asignatura("Matemáticas Avanzadas", "Facultad de Ciencias", 2L, "G01");
        Asignatura fisica = new Asignatura("Física Cuántica", "Facultad de Ciencias", 5L, "G02");
        Asignatura programacion = new Asignatura("Programación Java", "Facultad de Ingeniería", 4L, "G03");
        Asignatura historia = new Asignatura("Historia Universal", "Facultad de Humanidades", 6L, "G04");
        Asignatura economia = new Asignatura("Economía Internacional", "Facultad de Economía", 7L, "G05");
        
        // Guardar asignaturas
        matematicas = asignaturaRepository.save(matematicas);
        fisica = asignaturaRepository.save(fisica);
        programacion = asignaturaRepository.save(programacion);
        historia = asignaturaRepository.save(historia);
        economia = asignaturaRepository.save(economia);
        
        // Crear inscripciones de estudiantes
        // Estudiante 101 inscrito en 3 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(8L, matematicas));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(8L, programacion));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(8L, historia));
        
        // Estudiante 102 inscrito en 2 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(9L, fisica));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(9L, economia));
        
        // Estudiante 103 inscrito en 4 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(10L, matematicas));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(10L, fisica));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(10L, programacion));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(10L, economia));
        
        // Estudiante 104 inscrito en 1 asignatura
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(11L, historia));
        
        // Estudiante 105 inscrito en 2 asignaturas
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(11L, programacion));
        asignaturaEstudianteRepository.save(new AsignaturaEstudiante(11L, economia));
        
        System.out.println("Datos de ejemplo cargados correctamente");
        System.out.println("Asignaturas creadas: " + asignaturaRepository.count());
        System.out.println("Inscripciones creadas: " + asignaturaEstudianteRepository.count());
    }
}
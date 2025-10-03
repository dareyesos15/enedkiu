package com.asignaturas.service;

import com.asignaturas.entity.Asignatura;
import com.asignaturas.entity.AsignaturaEstudiante;
import com.asignaturas.repository.AsignaturaRepository;
import com.asignaturas.repository.AsignaturaEstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AsignaturaService {
    
    @Autowired
    private AsignaturaRepository asignaturaRepository;
    
    @Autowired
    private AsignaturaEstudianteRepository asignaturaEstudianteRepository;
    
    // Asignaturas
    public List<Asignatura> findAllAsignaturas() {
        return asignaturaRepository.findAll();
    }
    
    public Optional<Asignatura> findAsignaturaById(Long id) {
        return asignaturaRepository.findById(id);
    }
    
    public Asignatura saveAsignatura(Asignatura asignatura) {
        return asignaturaRepository.save(asignatura);
    }
    
    public void deleteAsignatura(Long id) {
        asignaturaRepository.deleteById(id);
    }
    
    public List<Asignatura> findAsignaturasByNombre(String nombre) {
        return asignaturaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public List<Asignatura> findAsignaturasByFacultad(String facultad) {
        return asignaturaRepository.findByFacultad(facultad);
    }
    
    // Gestión de estudiantes
    @Transactional
    public AsignaturaEstudiante inscribirEstudiante(Long asignaturaId, Long estudianteId) {
        Optional<Asignatura> asignatura = asignaturaRepository.findById(asignaturaId);
        if (asignatura.isPresent()) {
            if (!asignaturaEstudianteRepository.existsByEstudianteIdAndAsignaturaId(estudianteId, asignaturaId)) {
                AsignaturaEstudiante inscripcion = new AsignaturaEstudiante(estudianteId, asignatura.get());
                return asignaturaEstudianteRepository.save(inscripcion);
            }
            throw new RuntimeException("El estudiante ya está inscrito en esta asignatura");
        }
        throw new RuntimeException("Asignatura no encontrada");
    }
    
    @Transactional
    public void desinscribirEstudiante(Long asignaturaId, Long estudianteId) {
        asignaturaEstudianteRepository.deleteByEstudianteIdAndAsignaturaId(estudianteId, asignaturaId);
    }
    
    public List<Asignatura> findAsignaturasByEstudianteId(Long estudianteId) {
        return asignaturaRepository.findAsignaturasByEstudianteId(estudianteId);
    }
    
    public List<AsignaturaEstudiante> findEstudiantesByAsignaturaId(Long asignaturaId) {
        return asignaturaEstudianteRepository.findByAsignaturaId(asignaturaId);
    }
    
    public Long contarEstudiantesEnAsignatura(Long asignaturaId) {
        return asignaturaEstudianteRepository.countEstudiantesByAsignaturaId(asignaturaId);
    }
}

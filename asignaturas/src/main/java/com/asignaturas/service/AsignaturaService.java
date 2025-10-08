package com.asignaturas.service;

import com.asignaturas.entity.*;
import com.asignaturas.dto.*;
import com.asignaturas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignaturaService {
    
    @Autowired
    private AsignaturaRepository asignaturaRepository;
    
    @Autowired
    private AsignaturaEstudianteRepository asignaturaEstudianteRepository;
    
    @Autowired
    private SeguridadService seguridadService;

    // ========== ASIGNATURAS - CRUD ==========
    
    /**
     * Obtener todas las asignaturas (sin detalles)
     */
    public List<AsignaturaDTO> obtenerTodasAsignaturas() {
        return asignaturaRepository.findAll().stream()
                .map(AsignaturaDTO::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener asignatura por ID (sin detalles)
     */
    public Optional<AsignaturaDTO> obtenerAsignaturaPorId(Long id) {
        return asignaturaRepository.findById(id)
                .map(AsignaturaDTO::new);
    }
    
    /**
     * Obtener asignatura con todos los detalles (profesor y estudiantes)
     */
    public Optional<AsignaturaConDetallesDTO> obtenerAsignaturaConDetalles(Long id) {
        return asignaturaRepository.findById(id)
                .map(this::convertirAConDetalles);
    }
    
    /**
     * Crear nueva asignatura
     */
    public AsignaturaDTO crearAsignatura(AsignaturaDTO asignaturaDTO) {
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre(asignaturaDTO.getNombre());
        asignatura.setFacultad(asignaturaDTO.getFacultad());
        asignatura.setProfesorId(asignaturaDTO.getProfesorId());
        asignatura.setGrupo(asignaturaDTO.getGrupo());
        
        Asignatura saved = asignaturaRepository.save(asignatura);
        return new AsignaturaDTO(saved);
    }
    
    /**
     * Actualizar asignatura
     */
    public Optional<AsignaturaDTO> actualizarAsignatura(Long id, AsignaturaDTO asignaturaDTO) {
        return asignaturaRepository.findById(id)
                .map(asignaturaExistente -> {
                    asignaturaExistente.setNombre(asignaturaDTO.getNombre());
                    asignaturaExistente.setFacultad(asignaturaDTO.getFacultad());
                    asignaturaExistente.setProfesorId(asignaturaDTO.getProfesorId());
                    asignaturaExistente.setGrupo(asignaturaDTO.getGrupo());
                    
                    Asignatura updated = asignaturaRepository.save(asignaturaExistente);
                    return new AsignaturaDTO(updated);
                });
    }
    
    /**
     * Eliminar asignatura
     */
    public boolean eliminarAsignatura(Long id) {
        if (asignaturaRepository.existsById(id)) {
            asignaturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ========== BÚSQUEDAS ==========
    
    public List<AsignaturaDTO> buscarPorNombre(String nombre) {
        return asignaturaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(AsignaturaDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<AsignaturaDTO> buscarPorFacultad(String facultad) {
        return asignaturaRepository.findByFacultad(facultad).stream()
                .map(AsignaturaDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<AsignaturaDTO> buscarPorProfesor(Long profesorId) {
        return asignaturaRepository.findByProfesorId(profesorId).stream()
                .map(AsignaturaDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<AsignaturaDTO> buscarPorEstudiante(Long estudianteId) {
        return asignaturaRepository.findAsignaturasByEstudianteId(estudianteId).stream()
                .map(AsignaturaDTO::new)
                .collect(Collectors.toList());
    }

    // ========== GESTIÓN DE ESTUDIANTES ==========
    
    /**
     * Inscribir estudiante en asignatura
     */
    @Transactional
    public AsignaturaEstudianteDTO inscribirEstudiante(Long asignaturaId, Long estudianteId) {
        Optional<Asignatura> asignatura = asignaturaRepository.findById(asignaturaId);
        if (asignatura.isPresent()) {
            if (!asignaturaEstudianteRepository.existsByEstudianteIdAndAsignaturaId(estudianteId, asignaturaId)) {
                AsignaturaEstudiante inscripcion = new AsignaturaEstudiante(estudianteId, asignatura.get());
                AsignaturaEstudiante saved = asignaturaEstudianteRepository.save(inscripcion);
                return new AsignaturaEstudianteDTO(saved);
            }
            throw new RuntimeException("El estudiante ya está inscrito en esta asignatura");
        }
        throw new RuntimeException("Asignatura no encontrada");
    }
    
    /**
     * Desinscribir estudiante de asignatura
     */
    @Transactional
    public boolean desinscribirEstudiante(Long asignaturaId, Long estudianteId) {
        if (asignaturaEstudianteRepository.existsByEstudianteIdAndAsignaturaId(estudianteId, asignaturaId)) {
            asignaturaEstudianteRepository.deleteByEstudianteIdAndAsignaturaId(estudianteId, asignaturaId);
            return true;
        }
        return false;
    }
    
    /**
     * Obtener estudiantes de una asignatura (solo IDs)
     */
    public List<AsignaturaEstudianteDTO> obtenerEstudiantesDeAsignatura(Long asignaturaId) {
        return asignaturaEstudianteRepository.findByAsignaturaId(asignaturaId).stream()
                .map(AsignaturaEstudianteDTO::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener estudiantes de una asignatura con detalles completos
     */
    public List<UsuarioDTO> obtenerEstudiantesConDetalles(Long asignaturaId) {
        List<Long> estudianteIds = obtenerIdsEstudiantesPorAsignatura(asignaturaId);
        return seguridadService.obtenerEstudiantesPorIds(estudianteIds);
    }
    
    /**
     * Contar estudiantes en una asignatura
     */
    public Long contarEstudiantesEnAsignatura(Long asignaturaId) {
        return asignaturaEstudianteRepository.countEstudiantesByAsignaturaId(asignaturaId);
    }

    // ========== GESTIÓN DE PROFESORES ==========
    
    /**
     * Obtener profesor de una asignatura con detalles
     */
    public Optional<UsuarioDTO> obtenerProfesorDeAsignatura(Long asignaturaId) {
        return asignaturaRepository.findById(asignaturaId)
                .flatMap(asignatura -> seguridadService.obtenerProfesorDeAsignatura(asignatura.getProfesorId()));
    }

    // ========== MÉTODOS PRIVADOS AUXILIARES ==========
    
    private AsignaturaConDetallesDTO convertirAConDetalles(Asignatura asignatura) {
        AsignaturaConDetallesDTO dto = new AsignaturaConDetallesDTO(
            asignatura.getId(),
            asignatura.getNombre(),
            asignatura.getFacultad(),
            asignatura.getProfesorId(),
            asignatura.getGrupo()
        );
        
        // Obtener detalles del profesor
        seguridadService.obtenerProfesorDeAsignatura(asignatura.getProfesorId())
                .ifPresent(dto::setProfesor);
        
        // Obtener detalles de los estudiantes
        List<Long> estudianteIds = obtenerIdsEstudiantesPorAsignatura(asignatura.getId());
        List<UsuarioDTO> estudiantes = seguridadService.obtenerEstudiantesPorIds(estudianteIds);
        dto.setEstudiantes(estudiantes);
        dto.setTotalEstudiantes(estudiantes.size());
        
        return dto;
    }
    
    private List<Long> obtenerIdsEstudiantesPorAsignatura(Long asignaturaId) {
        return asignaturaEstudianteRepository.findByAsignaturaId(asignaturaId).stream()
                .map(AsignaturaEstudiante::getEstudianteId)
                .collect(Collectors.toList());
    }
}
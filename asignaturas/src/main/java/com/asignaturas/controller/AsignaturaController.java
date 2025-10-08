package com.asignaturas.controller;

import com.asignaturas.dto.*;
import com.asignaturas.service.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
@CrossOrigin(origins = "*")
public class AsignaturaController {
    
    @Autowired
    private AsignaturaService asignaturaService;

    // ========== ASIGNATURAS - CRUD COMPLETO ==========
    
    @GetMapping
    public List<AsignaturaDTO> obtenerTodasAsignaturas() {
        return asignaturaService.obtenerTodasAsignaturas();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> obtenerAsignaturaPorId(@PathVariable Long id) {
        return asignaturaService.obtenerAsignaturaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/detalles")
    public ResponseEntity<AsignaturaConDetallesDTO> obtenerAsignaturaConDetalles(@PathVariable Long id) {
        return asignaturaService.obtenerAsignaturaConDetalles(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public AsignaturaDTO crearAsignatura(@RequestBody AsignaturaDTO asignaturaDTO) {
        return asignaturaService.crearAsignatura(asignaturaDTO);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> actualizarAsignatura(
            @PathVariable Long id, 
            @RequestBody AsignaturaDTO asignaturaDTO) {
        return asignaturaService.actualizarAsignatura(id, asignaturaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsignatura(@PathVariable Long id) {
        return asignaturaService.eliminarAsignatura(id) 
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // ========== BÚSQUEDAS ==========
    
    @GetMapping("/buscar/nombre")
    public List<AsignaturaDTO> buscarPorNombre(@RequestParam String nombre) {
        return asignaturaService.buscarPorNombre(nombre);
    }
    
    @GetMapping("/buscar/facultad")
    public List<AsignaturaDTO> buscarPorFacultad(@RequestParam String facultad) {
        return asignaturaService.buscarPorFacultad(facultad);
    }
    
    @GetMapping("/buscar/profesor/{profesorId}")
    public List<AsignaturaDTO> buscarPorProfesor(@PathVariable Long profesorId) {
        return asignaturaService.buscarPorProfesor(profesorId);
    }
    
    @GetMapping("/buscar/estudiante/{estudianteId}")
    public List<AsignaturaDTO> buscarPorEstudiante(@PathVariable Long estudianteId) {
        return asignaturaService.buscarPorEstudiante(estudianteId);
    }

    // ========== GESTIÓN DE ESTUDIANTES ==========
    
    @PostMapping("/{asignaturaId}/estudiantes")
    public ResponseEntity<AsignaturaEstudianteDTO> inscribirEstudiante(
            @PathVariable Long asignaturaId, 
            @RequestBody InscripcionRequestDTO request) {
        try {
            AsignaturaEstudianteDTO inscripcion = asignaturaService.inscribirEstudiante(asignaturaId, request.getEstudianteId());
            return ResponseEntity.ok(inscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{asignaturaId}/estudiantes/{estudianteId}")
    public ResponseEntity<Void> desinscribirEstudiante(
            @PathVariable Long asignaturaId, 
            @PathVariable Long estudianteId) {
        return asignaturaService.desinscribirEstudiante(asignaturaId, estudianteId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{asignaturaId}/estudiantes")
    public List<AsignaturaEstudianteDTO> obtenerEstudiantesDeAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.obtenerEstudiantesDeAsignatura(asignaturaId);
    }
    
    @GetMapping("/{asignaturaId}/estudiantes/detalles")
    public List<UsuarioDTO> obtenerEstudiantesConDetalles(@PathVariable Long asignaturaId) {
        return asignaturaService.obtenerEstudiantesConDetalles(asignaturaId);
    }
    
    @GetMapping("/{asignaturaId}/estudiantes/count")
    public Long contarEstudiantesEnAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.contarEstudiantesEnAsignatura(asignaturaId);
    }

    // ========== GESTIÓN DE PROFESORES ==========
    
    @GetMapping("/{asignaturaId}/profesor")
    public ResponseEntity<UsuarioDTO> obtenerProfesorDeAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.obtenerProfesorDeAsignatura(asignaturaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
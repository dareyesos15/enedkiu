package com.asignaturas.controller;

import com.asignaturas.entity.Asignatura;
import com.asignaturas.entity.AsignaturaEstudiante;
import com.asignaturas.service.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asignaturas")
@CrossOrigin(origins = "*") // Para permitir requests desde frontend
public class AsignaturaController {
    
    @Autowired
    private AsignaturaService asignaturaService;
    
    // ASIGNATURAS - CRUD
    @GetMapping
    public List<Asignatura> getAllAsignaturas() {
        return asignaturaService.findAllAsignaturas();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Asignatura> getAsignaturaById(@PathVariable Long id) {
        Optional<Asignatura> asignatura = asignaturaService.findAsignaturaById(id);
        return asignatura.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Asignatura createAsignatura(@RequestBody Asignatura asignatura) {
        return asignaturaService.saveAsignatura(asignatura);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Asignatura> updateAsignatura(@PathVariable Long id, @RequestBody Asignatura asignaturaDetails) {
        Optional<Asignatura> asignatura = asignaturaService.findAsignaturaById(id);
        if (asignatura.isPresent()) {
            Asignatura asignaturaExistente = asignatura.get();
            asignaturaExistente.setNombre(asignaturaDetails.getNombre());
            asignaturaExistente.setFacultad(asignaturaDetails.getFacultad());
            asignaturaExistente.setProfesor(asignaturaDetails.getProfesor());
            asignaturaExistente.setGrupo(asignaturaDetails.getGrupo());
            return ResponseEntity.ok(asignaturaService.saveAsignatura(asignaturaExistente));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsignatura(@PathVariable Long id) {
        if (asignaturaService.findAsignaturaById(id).isPresent()) {
            asignaturaService.deleteAsignatura(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // BÚSQUEDAS
    @GetMapping("/buscar/nombre")
    public List<Asignatura> getAsignaturasByNombre(@RequestParam String nombre) {
        return asignaturaService.findAsignaturasByNombre(nombre);
    }
    
    @GetMapping("/buscar/facultad")
    public List<Asignatura> getAsignaturasByFacultad(@RequestParam String facultad) {
        return asignaturaService.findAsignaturasByFacultad(facultad);
    }
    
    // GESTIÓN DE ESTUDIANTES
    @PostMapping("/{asignaturaId}/estudiantes/{estudianteId}")
    public ResponseEntity<AsignaturaEstudiante> inscribirEstudiante(
            @PathVariable Long asignaturaId, 
            @PathVariable Long estudianteId) {
        try {
            AsignaturaEstudiante inscripcion = asignaturaService.inscribirEstudiante(asignaturaId, estudianteId);
            return ResponseEntity.ok(inscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{asignaturaId}/estudiantes/{estudianteId}")
    public ResponseEntity<Void> desinscribirEstudiante(
            @PathVariable Long asignaturaId, 
            @PathVariable Long estudianteId) {
        try {
            asignaturaService.desinscribirEstudiante(asignaturaId, estudianteId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/estudiante/{estudianteId}")
    public List<Asignatura> getAsignaturasByEstudiante(@PathVariable Long estudianteId) {
        return asignaturaService.findAsignaturasByEstudianteId(estudianteId);
    }
    
    @GetMapping("/{asignaturaId}/estudiantes")
    public List<AsignaturaEstudiante> getEstudiantesByAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.findEstudiantesByAsignaturaId(asignaturaId);
    }
    
    @GetMapping("/{asignaturaId}/estudiantes/count")
    public Long countEstudiantesByAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.contarEstudiantesEnAsignatura(asignaturaId);
    }
}
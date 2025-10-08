package com.asignaturas.service;

import com.asignaturas.dto.UsuarioDTO;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class SeguridadService {
    
    private final RestTemplate restTemplate;
    private final String SEGURIDAD_BASE_URL = "http://localhost:8000/api";
    
    //@Autowired
    public SeguridadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Obtener información de un usuario por ID desde el microservicio de seguridad
     */
    public Optional<UsuarioDTO> obtenerUsuarioPorId(Long usuarioId) {
        try {
            String url = SEGURIDAD_BASE_URL + "/users/" + usuarioId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                UsuarioDTO.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
            
            return Optional.empty();
            
        } catch (Exception e) {
            System.err.println("Error al obtener usuario con ID " + usuarioId + ": " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Obtener información de múltiples usuarios por sus IDs
     */
    public List<UsuarioDTO> obtenerUsuariosPorIds(List<Long> usuarioIds) {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        
        for (Long usuarioId : usuarioIds) {
            obtenerUsuarioPorId(usuarioId).ifPresent(usuarios::add);
        }
        
        return usuarios;
    }
    
    /**
     * Obtener el profesor de una asignatura
     */
    public Optional<UsuarioDTO> obtenerProfesorDeAsignatura(Long profesorId) {
        return obtenerUsuarioPorId(profesorId);
    }
    
    /**
     * Obtener todos los estudiantes de una asignatura
     */
    public List<UsuarioDTO> obtenerEstudiantesDeAsignatura(Long asignaturaId) {
        try {
            // Primero obtenemos los IDs de estudiantes desde nuestra base de datos
            // Luego consultamos el microservicio de seguridad para obtener sus detalles
            
            // Esta función asume que ya tenemos los IDs de estudiantes en nuestra base
            // La implementación completa dependerá de cómo obtengas los IDs de estudiantes
            
            // Ejemplo: Obtener estudiantes por algún criterio específico
            String url = SEGURIDAD_BASE_URL + "/estudiantes/asignatura/" + asignaturaId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<UsuarioDTO[]> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                UsuarioDTO[].class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            
            return new ArrayList<>();
            
        } catch (Exception e) {
            System.err.println("Error al obtener estudiantes de asignatura " + asignaturaId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtener estudiantes por sus IDs (versión alternativa)
     */
    public List<UsuarioDTO> obtenerEstudiantesPorIds(List<Long> estudianteIds) {
        return obtenerUsuariosPorIds(estudianteIds);
    }
}
package com.enedkiu.cursos.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String API_KEY_HEADER = "Authorization";
    private static final String API_KEY_PREFIX = "Bearer ";

    @Value("${app.api.key:}") // Valor por defecto vacío
    private String validApiKey;

    // Lista de endpoints públicos que no requieren API key
    private final List<String> publicEndpoints = Arrays.asList(
        "/health",
        "/actuator/health",
        "/actuator/info"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        
        // Verificar si el endpoint es público
        if (publicEndpoints.stream().anyMatch(requestUri::startsWith)) {
            return true;
        }

        // Verificar si la API key está configurada
        if (validApiKey == null || validApiKey.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"API Key no configurada en el servidor\"}");
            return false;
        }

        String authHeader = request.getHeader(API_KEY_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(API_KEY_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"API Key requerida en header Authorization\"}");
            return false;
        }

        String apiKey = authHeader.substring(API_KEY_PREFIX.length());
        
        if (!validApiKey.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"API Key inválida\"}");
            return false;
        }

        return true;
    }
}
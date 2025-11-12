package com.enedkiu.cursos.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(ApiKeyInterceptor.class.getName());
    private static final String API_KEY_HEADER = "Authorization";
    private static final String API_KEY_PREFIX = "Bearer ";

    @Value("${app.api.key}") 
    private String validApiKey;

    // Lista de endpoints públicos que no requieren API key
    private final List<String> publicEndpoints = Arrays.asList(
        "/health",
        "/actuator/health",
        "/actuator/info",
        "/profile",
        "/profile/config"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("🔍 === INTERCEPTOR EJECUTADO ===");
        System.out.println("🔍 URL: " + request.getRequestURI());
        System.out.println("🔍 API Key configurada: " + (validApiKey != null ? validApiKey : "NULL"));
        
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Header Auth: " + authHeader);

        String requestUri = request.getRequestURI();
        
        logger.info("=== INICIO INTERCEPTOR ===");
        logger.info("Request URI: " + requestUri);
        logger.info("API Key configurada: " + (validApiKey != null ? "SÍ" : "NO"));
        if (validApiKey != null) {
            logger.info("API Key valor: " + validApiKey);
        }
        
        // Verificar si el endpoint es público
        if (publicEndpoints.stream().anyMatch(requestUri::startsWith)) {
            logger.info("Endpoint público - acceso permitido");
            return true;
        }

        // Verificar si la API key está configurada
        if (validApiKey == null || validApiKey.isEmpty()) {
            logger.severe("API Key no configurada en el servidor");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"API Key no configurada en el servidor\"}");
            return false;
        }

        authHeader = request.getHeader(API_KEY_HEADER);
        logger.info("Header Authorization recibido: " + authHeader);
        
        if (authHeader == null || !authHeader.startsWith(API_KEY_PREFIX)) {
            logger.warning("API Key no proporcionada o formato incorrecto");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"API Key requerida en header Authorization\"}");
            return false;
        }

        String apiKey = authHeader.substring(API_KEY_PREFIX.length());
        logger.info("API Key recibida: " + apiKey);
        logger.info("API Key esperada: " + validApiKey);
        
        if (!validApiKey.equals(apiKey)) {
            logger.warning("API Key NO coincide");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"API Key inválida\"}");
            return false;
        }

        logger.info("API Key válida - acceso permitido");
        logger.info("=== FIN INTERCEPTOR ===");
        return true;
    }
}
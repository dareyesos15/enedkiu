<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Log;

class ReportesController extends Controller
{
    protected $reporteApiUrl;
    protected $apiKey; //clave de acceso

    public function __construct()
    {
        $this->reporteApiUrl = env('REPORTES_API_URL');
        $this->apiKey = env('API_KEY');
    }

    /**
     * Realiza una petición HTTP a la API de reportes
     */
    private function makeRequest($endpoint, $method = 'GET', $data = [])
    {
        try {
            $url = $this->reporteApiUrl . $endpoint;
            
            $headers = [
                'Authorization' => 'Bearer ' . $this->apiKey,
                'Accept' => 'application/json',
                'Content-Type' => 'application/json',
            ];

            $response = Http::withHeaders($headers)->$method($url, $data);

            if ($response->successful()) {
                // Para respuestas que no son JSON (como PDF o Excel), devolver la respuesta directamente
                $contentType = $response->header('Content-Type');
                
                if (str_contains($contentType, 'application/json')) {
                    return response()->json($response->json(), $response->status());
                } else {
                    // Para PDF, Excel, etc., devolver la respuesta binaria
                    return response($response->body(), $response->status())
                        ->withHeaders($response->headers());
                }
            } else {
                Log::error('Error en petición a reportes API', [
                    'endpoint' => $endpoint,
                    'method' => $method,
                    'status' => $response->status(),
                    'response' => $response->body()
                ]);
                
                return response()->json([
                    'error' => 'Error al comunicarse con el servicio de reportes',
                    'details' => $response->json() ?? $response->body()
                ], $response->status());
            }
        } catch (\Exception $e) {
            Log::error('Excepción en petición a reportes API', [
                'endpoint' => $endpoint,
                'method' => $method,
                'error' => $e->getMessage()
            ]);

            return response()->json([
                'error' => 'Error de conexión con el servicio de reportes',
                'details' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Construye la URL con el parámetro de formato
     */
    private function buildUrlWithFormat($baseEndpoint, $formato)
    {
        $validFormats = ['pdf', 'excel'];
        
        if (!in_array(strtolower($formato), $validFormats)) {
            return response()->json([
                'error' => 'Formato no válido',
                'message' => 'El formato debe ser "pdf" o "excel"'
            ], 400);
        }

        return $baseEndpoint . '?formato=' . $formato;
    }

    // ==================== ENDPOINTS DE REPORTES ====================

    /**
     * Generar reporte por estudiante
     */
    public function getReporteEstudiante($estudianteId, Request $request)
    {
        $formato = $request->query('formato', 'pdf'); // Valor por defecto: pdf
        
        $endpoint = $this->buildUrlWithFormat(
            "/reportes/estudiante/{$estudianteId}", 
            $formato
        );

        // Si buildUrlWithFormat devuelve una respuesta de error, la retornamos
        if ($endpoint instanceof \Illuminate\Http\JsonResponse) {
            return $endpoint;
        }

        return $this->makeRequest($endpoint);
    }

    /**
     * Generar reporte por curso
     */
    public function getReporteCurso($cursoId, Request $request)
    {
        $formato = $request->query('formato', 'pdf'); // Valor por defecto: pdf
        
        $endpoint = $this->buildUrlWithFormat(
            "/reportes/curso/{$cursoId}", 
            $formato
        );

        // Si buildUrlWithFormat devuelve una respuesta de error, la retornamos
        if ($endpoint instanceof \Illuminate\Http\JsonResponse) {
            return $endpoint;
        }

        return $this->makeRequest($endpoint);
    }

    /**
     * Generar reporte por tarea
     */
    public function getReporteTarea($tareaId, Request $request)
    {
        $formato = $request->query('formato', 'pdf'); // Valor por defecto: pdf
        
        $endpoint = $this->buildUrlWithFormat(
            "/reportes/tarea/{$tareaId}", 
            $formato
        );

        // Si buildUrlWithFormat devuelve una respuesta de error, la retornamos
        if ($endpoint instanceof \Illuminate\Http\JsonResponse) {
            return $endpoint;
        }

        return $this->makeRequest($endpoint);
    }

    /**
     * Método alternativo que acepta formato como parte de la ruta
     * Ejemplo: /api/reportes/estudiante/1/pdf
     */
    public function getReporteEstudianteWithFormat($estudianteId, $formato)
    {
        $endpoint = $this->buildUrlWithFormat(
            "/reportes/estudiante/{$estudianteId}", 
            $formato
        );

        if ($endpoint instanceof \Illuminate\Http\JsonResponse) {
            return $endpoint;
        }

        return $this->makeRequest($endpoint);
    }

    /**
     * Método alternativo que acepta formato como parte de la ruta
     * Ejemplo: /api/reportes/curso/1/excel
     */
    public function getReporteCursoWithFormat($cursoId, $formato)
    {
        $endpoint = $this->buildUrlWithFormat(
            "/reportes/curso/{$cursoId}", 
            $formato
        );

        if ($endpoint instanceof \Illuminate\Http\JsonResponse) {
            return $endpoint;
        }

        return $this->makeRequest($endpoint);
    }

    /**
     * Método alternativo que acepta formato como parte de la ruta
     * Ejemplo: /api/reportes/tarea/1/pdf
     */
    public function getReporteTareaWithFormat($tareaId, $formato)
    {
        $endpoint = $this->buildUrlWithFormat(
            "/reportes/tarea/{$tareaId}", 
            $formato
        );

        if ($endpoint instanceof \Illuminate\Http\JsonResponse) {
            return $endpoint;
        }

        return $this->makeRequest($endpoint);
    }
}
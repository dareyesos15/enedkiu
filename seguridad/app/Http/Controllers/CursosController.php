<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Log;

class CursosController extends Controller
{
    protected $cursosApiUrl;
    protected $apiKey; //clave de acceso

    public function __construct()
    {
        $this->cursosApiUrl = env('CURSOS_API_URL');
        $this->apiKey = env('API_KEY');
    }

    /**
     * Realiza una petición HTTP a la API de cursos
     */
    private function makeRequest($endpoint, $method = 'GET', $data = [])
    {
        try {
            $url = $this->cursosApiUrl . $endpoint;
            
            $headers = [
                'Authorization' => 'Bearer ' . $this->apiKey,
                'Accept' => 'application/json',
                'Content-Type' => 'application/json',
            ];

            $response = Http::withHeaders($headers)->$method($url, $data);

            if ($response->successful()) {
                return response()->json($response->json(), $response->status());
            } else {
                Log::error('Error en petición a cursos API', [
                    'endpoint' => $endpoint,
                    'method' => $method,
                    'status' => $response->status(),
                    'response' => $response->body()
                ]);
                
                return response()->json([
                    'error' => 'Error al comunicarse con el servicio de cursos',
                    'details' => $response->json() ?? $response->body()
                ], $response->status());
            }
        } catch (\Exception $e) {
            Log::error('Excepción en petición a cursos API', [
                'endpoint' => $endpoint,
                'method' => $method,
                'error' => $e->getMessage()
            ]);

            return response()->json([
                'error' => 'Error de conexión con el servicio de cursos',
                'details' => $e->getMessage()
            ], 500);
        }
    }

    // ==================== ASIGNATURAS ====================

    public function getAsignaturas()
    {
        return $this->makeRequest('/asignaturas');
    }

    public function getAsignatura($id)
    {
        return $this->makeRequest("/asignaturas/{$id}");
    }

    public function createAsignatura(Request $request)
    {
        return $this->makeRequest('/asignaturas', 'POST', $request->all());
    }

    public function updateAsignatura(Request $request, $id)
    {
        return $this->makeRequest("/asignaturas/{$id}", 'PUT', $request->all());
    }

    public function deleteAsignatura($id)
    {
        return $this->makeRequest("/asignaturas/{$id}", 'DELETE');
    }

    // ==================== CURSOS ====================

    public function getCursos()
    {
        return $this->makeRequest('/cursos');
    }

    public function getCurso($id)
    {
        return $this->makeRequest("/cursos/{$id}");
    }

    public function createCurso(Request $request)
    {
        return $this->makeRequest('/cursos', 'POST', $request->all());
    }

    public function updateCurso(Request $request, $id)
    {
        return $this->makeRequest("/cursos/{$id}", 'PUT', $request->all());
    }

    public function deleteCurso($id)
    {
        return $this->makeRequest("/cursos/{$id}", 'DELETE');
    }

    public function getTareasByCurso($cursoId)
    {
        return $this->makeRequest("/cursos/{$cursoId}/tareas");
    }

    public function getCursosByProfesor($profesorId)
    {
        return $this->makeRequest("/cursos/profesor/{$profesorId}");
    }

    public function getCursosByEstudiante($estudianteId)
    {
        return $this->makeRequest("/cursos/estudiante/{$estudianteId}");
    }

    // ==================== TAREAS ====================

    public function getTareas()
    {
        return $this->makeRequest('/tareas');
    }

    public function getTarea($id)
    {
        return $this->makeRequest("/tareas/{$id}");
    }

    public function createTarea(Request $request)
    {
        return $this->makeRequest('/tareas', 'POST', $request->all());
    }

    public function updateTarea(Request $request, $id)
    {
        return $this->makeRequest("/tareas/{$id}", 'PUT', $request->all());
    }

    public function deleteTarea($id)
    {
        return $this->makeRequest("/tareas/{$id}", 'DELETE');
    }

    public function getNotasByTarea($tareaId)
    {
        return $this->makeRequest("/tareas/{$tareaId}/notas");
    }

    // ==================== NOTAS ====================

    public function createNota(Request $request)
    {
        return $this->makeRequest('/notas', 'POST', $request->all());
    }

    public function getNota($id)
    {
        return $this->makeRequest("/notas/{$id}");
    }

    public function updateNota(Request $request, $id)
    {
        return $this->makeRequest("/notas/{$id}", 'PUT', $request->all());
    }

    public function deleteNota($id)
    {
        return $this->makeRequest("/notas/{$id}", 'DELETE');
    }

    public function getNotasByEstudiante($estudianteId)
    {
        return $this->makeRequest("/notas/estudiante/{$estudianteId}");
    }
}
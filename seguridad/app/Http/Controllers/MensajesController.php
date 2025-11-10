<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Log;

class MensajesController extends Controller
{
    protected $mensajesApiUrl;
    protected $apiKey; //clave de acceso

    public function __construct()
    {
        $this->mensajesApiUrl = env('MENSAJES_API_URL');
        $this->apiKey = env('API_KEY');
    }

    /**
     * Realiza una petición HTTP a la API de mensajes
     */
    private function makeRequest($endpoint, $method = 'GET', $data = [])
    {
        try {
            $url = $this->mensajesApiUrl . $endpoint;
            
            $headers = [
                'Authorization' => 'Bearer ' . $this->apiKey,
                'Accept' => 'application/json',
                'Content-Type' => 'application/json',
            ];

            $response = Http::withHeaders($headers)->$method($url, $data);

            if ($response->successful()) {
                return response()->json($response->json(), $response->status());
            } else {
                Log::error('Error en petición a mensajes API', [
                    'endpoint' => $endpoint,
                    'method' => $method,
                    'status' => $response->status(),
                    'response' => $response->body()
                ]);
                
                return response()->json([
                    'error' => 'Error al comunicarse con el servicio de mensajes',
                    'details' => $response->json() ?? $response->body()
                ], $response->status());
            }
        } catch (\Exception $e) {
            Log::error('Excepción en petición a mensajes API', [
                'endpoint' => $endpoint,
                'method' => $method,
                'error' => $e->getMessage()
            ]);

            return response()->json([
                'error' => 'Error de conexión con el servicio de mensajes',
                'details' => $e->getMessage()
            ], 500);
        }
    }

    // ==================== OPERACIONES CRUD PARA MENSAJES ====================

    /**
     * Obtener todos los mensajes
     */
    public function getMensajes()
    {
        return $this->makeRequest('/mensajes');
    }

    /**
     * Obtener un mensaje específico por ID
     */
    public function getMensaje($id)
    {
        return $this->makeRequest("/mensajes/{$id}");
    }

    /**
     * Crear un nuevo mensaje
     */
    public function createMensaje(Request $request)
    {
        return $this->makeRequest('/mensajes', 'POST', $request->all());
    }

    /**
     * Actualizar un mensaje existente
     */
    public function updateMensaje(Request $request, $id)
    {
        return $this->makeRequest("/mensajes/{$id}", 'PUT', $request->all());
    }

    /**
     * Eliminar un mensaje
     */
    public function deleteMensaje($id)
    {
        return $this->makeRequest("/mensajes/{$id}", 'DELETE');
    }

    // ==================== ENDPOINTS ESPECÍFICOS ====================

    /**
     * Obtener mensajes por remitente
     */
    public function getMensajesByRemitente($remitenteId)
    {
        return $this->makeRequest("/mensajes/remitente/{$remitenteId}");
    }

    /**
     * Obtener mensajes por receptor
     */
    public function getMensajesByReceptor($receptorId)
    {
        return $this->makeRequest("/mensajes/receptor/{$receptorId}");
    }
}
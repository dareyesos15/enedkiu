<?php
// app/Http/Middleware/AdminOrProfessor.php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class AdminOrProfessor
{
    public function handle(Request $request, Closure $next): Response
    {
        $user = $request->user();

        if (!$user) {
            return response()->json(['message' => 'Unauthenticated'], 401);
        }

        // Verificar si el usuario tiene rol de admin O professor
        if (!$user->hasRole('admin') && !$user->hasRole('professor')) {
            return response()->json([
                'message' => 'No tienes permisos para acceder a este recurso. Se requiere rol de administrador o profesor.'
            ], 403);
        }

        return $next($request);
    }
}
<?php
// app/Http/Middleware/AdminOnly.php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class AdminOnly
{
    public function handle(Request $request, Closure $next): Response
    {
        $user = $request->user();

        if (!$user) {
            return response()->json(['message' => 'Unauthenticated'], 401);
        }

        // Verificar si el usuario tiene rol de admin
        if (!$user->hasRole('admin')) {
            return response()->json([
                'message' => 'No tienes permisos para acceder a este recurso. Se requiere rol de administrador.'
            ], 403);
        }

        return $next($request);
    }
}
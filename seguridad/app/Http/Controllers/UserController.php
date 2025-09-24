<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class UserController extends Controller
{
    public function get_users(){
        $users = User::all();
        return response()->json($users, 200);
    }

    public function create_user(Request $request)
    {
        $user = new User;
        $user->name = $request->name;
        $user->email = $request->email;
        //$user->email = $request->role;
        $user->password = bcrypt($request->password);
        $user->save();
        return response()->json($user, 201);
    }

    public function login(Request $request)
    {
        $user = User::where('email', $request->email)->first();
        if (! $user || ! Hash::check($request->password, $user->password)) {
            return response()->json(['message' => 'Credenciales inválidas'], 401);
        }
        $token = $user->createToken('auth_token')->plainTextToken;
        return response()->json([
            'access_token' => $token,
            'user_name' => $user->name
        ]);
    }

    public function logout(Request $request)
    {
        try {
            // Verificar si el usuario está autenticado
            if (!$request->user()) {
                return response()->json(['message' => 'Usuario no autenticado'], 401);
            }

            // Eliminar el token actual
            $request->user()->currentAccessToken()->delete();
            
            return response()->json(['message' => 'Sesión cerrada correctamente']);
            
        } catch (\Exception $e) {
            return response()->json(['message' => 'Error al cerrar sesión'], 500);
        }
    }

    /**
     * Cambiar contraseña del usuario autenticado
     */
    public function change_password(Request $request)
    {
        // 1. Validar los datos de entrada
        $validator = Validator::make($request->all(), [
            'current_password' => 'required|string',
            'new_password' => 'required|string|min:6|confirmed',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        // 2. Obtener el usuario autenticado
        $user = $request->user();

        // 3. Verificar que la contraseña actual es correcta
        if (!Hash::check($request->current_password, $user->password)) {
            return response()->json([
                'message' => 'La contraseña actual es incorrecta'
            ], 401);
        }

        // 4. Verificar que la nueva contraseña no sea igual a la actual
        if (Hash::check($request->new_password, $user->password)) {
            return response()->json([
                'message' => 'La nueva contraseña no puede ser igual a la actual'
            ], 422);
        }

        // 5. Actualizar la contraseña
        $user->password = Hash::make($request->new_password);
        $user->save();

        // 6. Opcional: eliminar tokens existentes (logout forzado)
        // $user->tokens()->delete();

        return response()->json([
            'message' => 'Contraseña cambiada exitosamente'
        ], 200);
    }
}

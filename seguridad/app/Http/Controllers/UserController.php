<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use App\Models\Role;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class UserController extends Controller
{
    public function get_user($id)
    {
        // Cargar el usuario con su relación de roles
        $user = User::with('roles')->find($id);

        if (!$user) {
            return response()->json(['message' => 'User not found'], 404);
        }

        // Añadir el campo 'rol' con el nombre del primer rol encontrado
        $user->rol = $user->roles->first()->name ?? null;

        return response()->json($user, 200);
    }

    public function get_users()
    {
        // Cargar todos los usuarios con su relación de roles
        $users = User::with('roles')->get();

        // Mapear la colección para añadir el campo 'rol' a cada usuario
        $users->map(function ($user) {
            $user->rol = $user->roles->first()->name ?? null;
            return $user;
        });

        return response()->json($users, 200);
    }

    /**
     * Obtener todos los usuarios con rol de profesor
     */
    public function get_professors()
    {
        $professors = User::whereHas('roles', function ($query) {
            $query->where('name', 'professor');
        })->get();

        // Mapear la colección para añadir explícitamente el campo 'rol'
        $professors->map(function ($professor) {
            $professor->rol = 'professor';
            return $professor;
        });

        return response()->json([
            'professors' => $professors,
            'count' => $professors->count()
        ], 200);
    }

    /**
     * Obtener todos los usuarios con rol de estudiante
     */
    public function get_students()
    {
        $students = User::whereHas('roles', function ($query) {
            $query->where('name', 'student');
        })->get();

        // Mapear la colección para añadir explícitamente el campo 'rol'
        $students->map(function ($student) {
            $student->rol = 'student';
            return $student;
        });

        return response()->json([
            'students' => $students,
            'count' => $students->count()
        ], 200);
    }

    /**
     * Crear un nuevo usuario y asignarle por defecto el rol de estudiante (ID 2).
     */
    public function create_user(Request $request)
    {
        // 1. Validar los datos de entrada
        $validator = Validator::make($request->all(), [
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => 'required|string|min:8',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        // 2. Crear el usuario
        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password), // Usar Hash::make en lugar de bcrypt
        ]);

        // 3. Asignar el rol por defecto: Estudiante (ID 2)
        // Usamos el método attach para relacionar el usuario con el rol en la tabla intermedia
        $student_role_id = 3; 
        try {
            // Se asume que el ID 2 existe en la tabla roles
            $user->roles()->attach($student_role_id); 
        } catch (\Exception $e) {
            // Opcional: Manejo de error si el rol no existe, aunque en un sistema real debería existir
            \Log::error('Role ID 3 (Student) not found for user creation: ' . $e->getMessage());
            // Se podría eliminar el usuario creado o devolver un error
        }
        
        // Cargar el rol para la respuesta
        $user->load('roles'); 

        return response()->json([
            'message' => 'Usuario creado exitosamente con rol de estudiante',
            'user' => $user->only(['id', 'name', 'email', 'roles'])
        ], 201);
    }
    
    /**
     * Actualizar los datos de un usuario existente.
     */
    public function update_user(Request $request, $id)
    {
        // 1. Encontrar el usuario
        $user = User::find($id);

        if (!$user) {
            return response()->json(['message' => 'User not found'], 404);
        }

        // 2. Validar los datos de entrada
        // 'email' es único excepto para el usuario actual
        $validator = Validator::make($request->all(), [
            'name' => 'sometimes|required|string|max:255',
            'email' => 'sometimes|required|string|email|max:255|unique:users,email,'.$user->id,
            'password' => 'nullable|string|min:8', // Permite actualizar o no
            'role_id' => 'nullable|integer|exists:roles,id', // Permite cambiar el rol
        ]);

        if ($validator->fails()) {
            return response()->json([
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        // 3. Actualizar los datos del usuario
        $user->name = $request->input('name', $user->name);
        $user->email = $request->input('email', $user->email);
        
        if ($request->filled('password')) {
            $user->password = Hash::make($request->password);
        }
        
        $user->save();

        // 4. Actualizar el rol si se proporciona 'role_id'
        if ($request->filled('role_id')) {
            // El método sync desasocia todos los roles actuales y adjunta solo el nuevo/s.
            // Si solo permites un rol por usuario, 'sync' es ideal.
            $user->roles()->sync([(int)$request->role_id]);
        }
        
        // 5. Cargar el rol para la respuesta
        $user->load('roles'); 

        return response()->json([
            'message' => 'Usuario actualizado exitosamente',
            'user' => $user->only(['id', 'name', 'email', 'roles'])
        ], 200);
    }
    
    /**
     * Eliminar un usuario por su ID.
     */
    public function delete_user($id)
    {
        // 1. Encontrar el usuario
        $user = User::find($id);

        if (!$user) {
            return response()->json(['message' => 'User not found'], 404);
        }
        
        // 2. Desasociar todos los roles del usuario
        // Es una buena práctica limpiar las relaciones Many-to-Many antes de eliminar el modelo
        $user->roles()->detach();

        // 3. Eliminar el usuario
        $user->delete();

        return response()->json(['message' => 'Usuario eliminado exitosamente'], 200);
    }

    public function login(Request $request)
    {
        $user = User::where('email', $request->email)->first();
        if (!$user || !Hash::check($request->password, $user->password)) {
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
            $token = $request->user()->currentAccessToken();
            if ($token) {
                $token->delete();
            }

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
<?php
// database/seeders/UserRoleSeeder.php

namespace Database\Seeders;

use App\Models\User;
use App\Models\Role;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class UserRoleSeeder extends Seeder
{
    public function run()
    {
        // Obtener los usuarios
        $david = User::where('email', 'david@example.com')->first();
        $pedro = User::where('email', 'pedro@example.com')->first();
        $maria = User::where('email', 'maria@example.com')->first();

        // Obtener los roles
        $adminRole = Role::where('name', 'admin')->first();
        $professorRole = Role::where('name', 'professor')->first();
        $studentRole = Role::where('name', 'student')->first();

        // Verificar que existen todos los registros
        if ($david && $adminRole) {
            $david->roles()->syncWithoutDetaching([$adminRole->id]);
            $this->command->info("Rol admin asignado a David");
        }

        if ($pedro && $professorRole) {
            $pedro->roles()->syncWithoutDetaching([$professorRole->id]);
            $this->command->info("Rol professor asignado a Pedro");
        }

        if ($maria && $studentRole) {
            $maria->roles()->syncWithoutDetaching([$studentRole->id]);
            $this->command->info("Rol student asignado a Maria");
        }

        // --- Asignación de roles a nuevos usuarios ---

        // 1 nuevo admin
        $newAdmin = User::where('email', 'newadmin@example.com')->first();
        if ($newAdmin && $adminRole) {
            $newAdmin->roles()->syncWithoutDetaching([$adminRole->id]);
            $this->command->info("Rol admin asignado a Nuevo Admin");
        }

        // 3 nuevos profesores
        $newProfessors = User::whereIn('email', [
            'profesor1@example.com',
            'profesor2@example.com',
            'profesor3@example.com'
        ])->get();
        if ($professorRole) {
            foreach ($newProfessors as $professor) {
                $professor->roles()->syncWithoutDetaching([$professorRole->id]);
                $this->command->info("Rol professor asignado a " . $professor->name);
            }
        }

        // 13 nuevos estudiantes
        $newStudents = User::whereIn('email', [
            'estudiante1@example.com',
            'estudiante2@example.com',
            'estudiante3@example.com',
            'estudiante4@example.com',
            'estudiante5@example.com',
            'estudiante6@example.com',
            'estudiante7@example.com',
            'estudiante8@example.com',
            'estudiante9@example.com',
            'estudiante10@example.com',
            'estudiante11@example.com',
            'estudiante12@example.com',
            'estudiante13@example.com'
        ])->get();
        if ($studentRole) {
            foreach ($newStudents as $student) {
                $student->roles()->syncWithoutDetaching([$studentRole->id]);
                $this->command->info("Rol student asignado a " . $student->name);
            }
        }
    }
}
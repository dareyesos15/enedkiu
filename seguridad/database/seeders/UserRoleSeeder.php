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
    }
}
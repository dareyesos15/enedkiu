<?php
// database/seeders/RoleSeeder.php

namespace Database\Seeders;

use App\Models\Role;
use Illuminate\Database\Seeder;

class RoleSeeder extends Seeder
{
    public function run()
    {
        $roles = [
            [
                'name' => 'admin',
                'description' => 'Administrador del sistema'
            ],
            [
                'name' => 'professor',
                'description' => 'Profesor'
            ],
            [
                'name' => 'student',
                'description' => 'Estudiante'
            ]
        ];

        foreach ($roles as $role) {
            Role::create($role);
        }
    }
}
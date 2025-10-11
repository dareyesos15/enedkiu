<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Seeder;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $users = [
            [
                'name' => 'David',
                'email' => 'david@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Pedro',
                'email' => 'pedro@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Maria',
                'email' => 'maria@example.com',
                'password' => bcrypt('password')
            ],

            [
                'name' => 'Nuevo Admin',
                'email' => 'newadmin@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Profesor Uno',
                'email' => 'profesor1@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Profesor Dos',
                'email' => 'profesor2@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Profesor Tres',
                'email' => 'profesor3@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 1',
                'email' => 'estudiante1@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 2',
                'email' => 'estudiante2@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 3',
                'email' => 'estudiante3@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 4',
                'email' => 'estudiante4@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 5',
                'email' => 'estudiante5@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 6',
                'email' => 'estudiante6@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 7',
                'email' => 'estudiante7@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 8',
                'email' => 'estudiante8@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 9',
                'email' => 'estudiante9@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 10',
                'email' => 'estudiante10@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 11',
                'email' => 'estudiante11@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 12',
                'email' => 'estudiante12@example.com',
                'password' => bcrypt('password')
            ],
            [
                'name' => 'Estudiante 13',
                'email' => 'estudiante13@example.com',
                'password' => bcrypt('password')
            ],
        ];

        foreach ($users as $user) {
            User::firstOrCreate(['email' => $user['email']], $user);
        }
    }
}

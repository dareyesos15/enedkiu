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
            ]
        ];

        foreach ($users as $user) {
            User::create($user);
        }
    }
}

<?php
// routes/api.php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;

Route::post('/create_user', [UserController::class, 'create_user']);
Route::post('/login', [UserController::class, 'login']);
Route::get('/users', [UserController::class, 'get_users']);
Route::get('/professors', [UserController::class, 'get_professors']);
Route::get('/students', [UserController::class, 'get_students']);
Route::get('/user/{id}', [UserController::class, 'get_user']);

Route::group(['middleware' => 'auth:sanctum'], function () {
    Route::post('/logout', [UserController::class, 'logout']);
    Route::post('/change_password', [UserController::class, 'change_password']);
});
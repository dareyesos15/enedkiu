<?php
// routes/api.php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;

Route::get('/users', [UserController::class, 'get_users']);
Route::get('/user/{id}', [UserController::class, 'get_user']);
Route::post('/create_user', [UserController::class, 'create_user']);
Route::put('/user/{id}', [UserController::class, 'update_user']); 
Route::delete('/user/{id}', [UserController::class, 'delete_user']); 


Route::get('/professors', [UserController::class, 'get_professors']);
Route::get('/students', [UserController::class, 'get_students']);

Route::post('/login', [UserController::class, 'login']);

Route::group(['middleware' => 'auth:sanctum'], function () {
    Route::post('/logout', [UserController::class, 'logout']);
    Route::post('/change_password', [UserController::class, 'change_password']);
});
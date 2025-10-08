<?php
// routes/api.php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;

Route::post('/create_user', [UserController::class,'create_user']);
Route::post('/login', [UserController::class,'login']);

Route::group(['middleware' => 'auth:sanctum'], function () {
    Route::post('/logout', [UserController::class,'logout']);
    Route::post('/change_password', [UserController::class,'change_password']);
    
    // Solo Admin puede acceder a estas rutas
    Route::group(['middleware' => 'admin.only'], function () {
        Route::get('/{id}', [UserController::class,'get_user']);
        Route::get('/users', [UserController::class,'get_users']);
        Route::get('/professors', [UserController::class,'get_professors']);
    });
    
    // Admin o Professor pueden acceder a esta ruta
    Route::group(['middleware' => 'admin.or.professor'], function () {
        Route::get('/students', [UserController::class,'get_students']);
    });
});
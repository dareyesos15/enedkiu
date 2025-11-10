<?php
// routes/api.php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\CursosController;
use App\Http\Controllers\MensajesController;
use App\Http\Controllers\ReportesController;

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

//Apigateway: rutas de microservicio de cursos
Route::prefix('cursos')->group(function () {
    // Asignaturas
    Route::get('/asignaturas', [CursosController::class, 'getAsignaturas']);
    Route::get('/asignaturas/{id}', [CursosController::class, 'getAsignatura']);
    Route::post('/asignaturas', [CursosController::class, 'createAsignatura']);
    Route::put('/asignaturas/{id}', [CursosController::class, 'updateAsignatura']);
    Route::delete('/asignaturas/{id}', [CursosController::class, 'deleteAsignatura']);
    
    // Cursos
    Route::get('/cursos', [CursosController::class, 'getCursos']);
    Route::get('/cursos/{id}', [CursosController::class, 'getCurso']);
    Route::post('/cursos', [CursosController::class, 'createCurso']);
    Route::put('/cursos/{id}', [CursosController::class, 'updateCurso']);
    Route::delete('/cursos/{id}', [CursosController::class, 'deleteCurso']);
    Route::get('/cursos/{cursoId}/tareas', [CursosController::class, 'getTareasByCurso']);
    Route::get('/cursos/profesor/{profesorId}', [CursosController::class, 'getCursosByProfesor']);
    Route::get('/cursos/estudiante/{estudianteId}', [CursosController::class, 'getCursosByEstudiante']);
    
    // Tareas
    Route::get('/tareas', [CursosController::class, 'getTareas']);
    Route::get('/tareas/{id}', [CursosController::class, 'getTarea']);
    Route::post('/tareas', [CursosController::class, 'createTarea']);
    Route::put('/tareas/{id}', [CursosController::class, 'updateTarea']);
    Route::delete('/tareas/{id}', [CursosController::class, 'deleteTarea']);
    Route::get('/tareas/{tareaId}/notas', [CursosController::class, 'getNotasByTarea']);
    
    // Notas
    Route::post('/notas', [CursosController::class, 'createNota']);
    Route::get('/notas/{id}', [CursosController::class, 'getNota']);
    Route::put('/notas/{id}', [CursosController::class, 'updateNota']);
    Route::delete('/notas/{id}', [CursosController::class, 'deleteNota']);
    Route::get('/notas/estudiante/{estudianteId}', [CursosController::class, 'getNotasByEstudiante']);
});

//Apigateway: rutas de microservicio de cursos
Route::prefix('mensajes')->group(function () {
    // Operaciones CRUD básicas
    Route::get('/', [MensajesController::class, 'getMensajes']);
    Route::get('/{id}', [MensajesController::class, 'getMensaje']);
    Route::post('/', [MensajesController::class, 'createMensaje']);
    Route::put('/{id}', [MensajesController::class, 'updateMensaje']);
    Route::delete('/{id}', [MensajesController::class, 'deleteMensaje']);
    
    // Endpoints específicos
    Route::get('/remitente/{remitenteId}', [MensajesController::class, 'getMensajesByRemitente']);
    Route::get('/receptor/{receptorId}', [MensajesController::class, 'getMensajesByReceptor']);
});

//Apigateway: rutas de microservicio de cursos
Route::prefix('reportes')->group(function () {
    // Rutas con formato como query parameter (recomendado)
    Route::get('/estudiante/{estudianteId}', [ReportesController::class, 'getReporteEstudiante']);
    Route::get('/curso/{cursoId}', [ReportesController::class, 'getReporteCurso']);
    Route::get('/tarea/{tareaId}', [ReportesController::class, 'getReporteTarea']);
    
    // Rutas alternativas con formato en la URL (opcional)
    Route::get('/estudiante/{estudianteId}/{formato}', [ReportesController::class, 'getReporteEstudianteWithFormat']);
    Route::get('/curso/{cursoId}/{formato}', [ReportesController::class, 'getReporteCursoWithFormat']);
    Route::get('/tarea/{tareaId}/{formato}', [ReportesController::class, 'getReporteTareaWithFormat']);
});
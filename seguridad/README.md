# Microservicio de seguridad y gestión de usuarios de enedkiu

## Tecnologías
**Php-Laravel + MySql**

## Dependencias
- **php8:** https://www.php.net/downloads.php
- **composer:** https://getcomposer.org/download/ 
- **laragon:** https://laragon.org/download

## levantar el microservicio en servidor local
- **Instalar dependencias de laravel:** `composer update`
- **Iniciar servicio:** `php artisan serve`

## Servidor de desarrollo
`http://localhost:8000`

## Endpoints 
- /api/users
- /api/students
- /api/professors
- /api/user/{id}
- /api/create_user
- /api/login.

**Requieren token**
- /api/logout
- /api/change_password

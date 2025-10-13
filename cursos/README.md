# Microservicio de Cursos, asignaturas y notas de enedkiu

## Tecnologías
**Java-SpringBoot + MySql**

## Dependencias
- **Java JDK-21:** https://www.php.net/downloads.php
- **laragon:** https://laragon.org/download

## levantar el microservicio en servidor local
`./mvnw spring-boot:run` (en gitbash)

## Servidor de desarrollo
`http://localhost:8080`

## Endpoints 
**Tabla asignaturas**
- /api/asignaturas (REST)

**Tabla cursos**
- /api/cursos (REST)
- /api/cursos/{cursoId}/tareas (Tareas de un curso)
- /api/cursos/profesor/{profesorId} (Cursos del profesor)
- /api/cursos/estudiante/{estudianteId} (Cursos del estudiante)

**Tabla tareas** 
- /api/tareas (REST)
- /api/tareas/{tareaid}/notas

**Tabla notas**
- /api/notas (REST sin GET para todas las notas)
- /api/notas/estudiante/{estudianteId} (Notas de un estudiante)
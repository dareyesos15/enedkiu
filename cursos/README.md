# Microservicio de Cursos, asignaturas y notas

## levantar el microservicio en servidor local
`./mvnw spring-boot:run`

## Dirección Ip y puerto por defecto
`http://localhost:8080`

## Endpoints 
- /api/asignaturas (REST)

- /api/cursos (REST)
- /api/cursos/{cursoId}/tareas (Tareas de un curso)
- /api/cursos/profesor/{profesorId} (Cursos del profesor)
- /api/cursos/estudiante/{estudianteId} (Cursos del estudiante)

- /api/tareas (REST)
- /api/tareas/{tareaid}/notas

- /api/notas (REST sin getAll)
- /api/notas/estudiante/{estudianteId} (Notas de un estudiante)

## Ejecución del microservicio en entorno local
**Instalación**
Se debe tener instalado JDK 21.

**Dependencias**
Se debe tener corriendo el microservicio de seguridad y crear una base de datos mysql llamada asignaturas. Para configurar la base de datos se debe crear un archivo application-dev.properties (ver archivo application.properties.example) en la ruta /asignaturas/src/java/com/asignaturas/resources 

**Comandos para inciar microservicio**

*Windows*

- *PowerShell*
`$env:SPRING_PROFILES_ACTIVE="dev"`
`.\mvnw.cmd spring-boot:run`

*Unix*
`SPRING_PROFILES_ACTIVE=dev`
`./mvnw spring-boot:run`


**Url local**

*http://localhost:8080/api/asignaturas*

## Endpoint y rutas

**ASIGNATURAS (CRUD):**
GET    /api/asignaturas                    # Todas las asignaturas
GET    /api/asignaturas/{id}              # Asignatura por ID
GET    /api/asignaturas/{id}/detalles     # Asignatura con detalles completos
POST   /api/asignaturas                   # Crear asignatura
PUT    /api/asignaturas/{id}              # Actualizar asignatura
DELETE /api/asignaturas/{id}              # Eliminar asignatura

**BÚSQUEDAS:**
GET    /api/asignaturas/buscar/nombre?nombre=X
GET    /api/asignaturas/buscar/facultad?facultad=X
GET    /api/asignaturas/buscar/profesor/{profesorId}
GET    /api/asignaturas/buscar/estudiante/{estudianteId}

**ESTUDIANTES:**
POST   /api/asignaturas/{id}/estudiantes           # Inscribir estudiante
DELETE /api/asignaturas/{id}/estudiantes/{estId}   # Desinscribir estudiante
GET    /api/asignaturas/{id}/estudiantes           # Estudiantes (solo IDs)
GET    /api/asignaturas/{id}/estudiantes/detalles  # Estudiantes con detalles
GET    /api/asignaturas/{id}/estudiantes/count     # Contar estudiantes

**PROFESORES:**
GET    /api/asignaturas/{id}/profesor              # Profesor con detalles
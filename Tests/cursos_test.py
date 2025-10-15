from locust import HttpUser, task, between
import uuid # ⬅️ Importar la librería para IDs únicas
import json

class CursoTasks(HttpUser):

    # Se inicializan las variables al inicio de la simulación para cada usuario
    def on_start(self):
        self.curso_id = None # Almacenará la ID del curso creado
    
    # Tarea 1: GET (Leer)
    @task # Mayor peso (más lecturas)
    def metodo_get(self):
        """Obtiene la lista de todos los cursos."""
        self.client.get('/api/cursos')

    # Tarea 2: POST (Crear)
    @task
    def metodo_post(self):
        """Crea un nuevo curso con un nombre único y guarda su ID."""
        
        # 🔑 Solución al error de duplicado: Generar un UUID único para el nombre.
        unique_name = f"grupo-prueba-{uuid.uuid4().hex}"
        
        data = {
            "nombre": unique_name, # ⬅️ Nombre único
            "estudiantesId": [20],
            "profesorId": 1,
            "tareas": [],
            "asignatura": {"id": 4}
        }
        
        response = self.client.post('/api/cursos', json=data)
        
        # Almacenar la ID si la creación fue exitosa (código 200)
        if response.status_code == 200:
            try:
                curso_creado = response.json()
                self.curso_id = curso_creado.get("id")
                # print(f"Curso creado con ID: {self.curso_id}") # Para debugging
            except json.JSONDecodeError:
                self.curso_id = None
        else:
            self.curso_id = None
            
    # Tarea 3: PUT (Actualizar)
    @task
    def metodo_put(self):
        """Actualiza el curso creado usando su ID."""
        # Solo intenta actualizar si un curso ha sido creado exitosamente
        if self.curso_id:
            # Generamos un nuevo nombre único para la actualización, si es necesario
            update_name = f"grupo-actualizado-{uuid.uuid4().hex}"
            
            update_data = {
                # Aseguramos que el nombre actualizado también sea único
                "nombre": update_name, 
                "estudiantesId": [20, 21], # Ejemplo de cambio
                "profesorId": 1,
                "tareas": [],
                "asignatura": {"id": 4}
            }
            # Llama al endpoint con la ID en la URL
            self.client.put(f'/api/cursos/{self.curso_id}', json=update_data)

    # Tarea 4: DELETE (Eliminar)
    @task
    def metodo_delete(self):
        """Elimina el curso creado, completando el ciclo CRUD."""
        # Solo intenta eliminar si un curso ha sido creado
        if self.curso_id:
            response = self.client.delete(f'/api/cursos/{self.curso_id}')
            
            # Si la eliminación es exitosa (200 OK o 204 No Content), 
            # reseteamos la ID para que el usuario pueda crear uno nuevo en el siguiente ciclo POST.
            if response.status_code in [200, 204]:
                self.curso_id = None
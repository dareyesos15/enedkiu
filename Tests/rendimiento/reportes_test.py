from locust import HttpUser, task, between

class ReportesTasks(HttpUser):
    """
    Conjunto de tareas para simular la interacción con la API de reportes.
    """

    # --- 1. GET /reportes/curso/<int:curso_id> (obtener reporte de un curso) ---
    @task
    def reporte_curso(self):
        self.client.get('/reportes/curso/1')
    
    # --- 2. GET /reportes/estudiante/<int:estudiante_id> (obtener reporte de un estudiante) ---
    @task
    def reporte_estudiante(self):
        self.client.get('/reportes/estudiante/20')

    # --- 3. GET /reportes/tarea/<int:tarea_id> (obtener reporte de una tarea) ---
    @task
    def reporte_tarea(self):
        self.client.get('/reportes/tarea/1?formatp=excel')
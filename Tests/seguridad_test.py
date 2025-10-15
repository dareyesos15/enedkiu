import uuid
from locust import HttpUser, task, between
import random
import json

# Constante para el rol por defecto (estudiante)
DEFAULT_ROLE_ID = 3

# Lista para almacenar los IDs de usuarios creados durante la prueba
created_user_ids = []

class UserTasks(HttpUser):
    """
    Clase que define las tareas de la API para los usuarios de Locust.
    """
    # --- 1. POST /create_user (Crear usuario) ---
    @task
    def create_user(self):
        unique = uuid.uuid4().hex
        user_data = {
            "name": f"TestUser{unique}",
            "email": f"user{unique}@example.com",
            "password": "PasswordSegura123" 
        }

        self.client.post('/create_user', json=user_data)

    # --- 2. GET /user/{id} (Obtener usuario) ---
    @task
    def get_user(self):
        self.client.get(f'/user/1')

    # --- 3. PUT /user/{id} (Actualizar usuario) ---
    @task
    def update_user(self):
        unique = uuid.uuid4().hex
        user_data = {
            "name": f"TestUserUpdated{unique}",
            "email": f"user{unique}@example.com",
            "password": "PasswordSegura123" 
        }
        self.client.put(f'/user/1', json=user_data)
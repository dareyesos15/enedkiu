from locust import HttpUser, task, between
import random
import json
import logging

logging.basicConfig(level=logging.INFO)

# Lista global para almacenar los IDs de mensajes creados
created_message_ids = []

class MensajesTasks(HttpUser):
    """
    Conjunto de tareas para simular la interacción con la API de mensajes.
    """

    # --- 1. POST /mensajes (Crear mensaje) ---
    @task
    def create_message(self):
        """Crea un nuevo mensaje y guarda su ID."""
        unique_id = random.randint(1000, 9999)
        
        message_data = {
            "remitente_id": random.choice([101, 102, 103]),
            "receptores_id": random.choice([201, 202, 203]),
            "asunto": f"Prueba de carga {unique_id}",
            "cuerpo": f"Este es un mensaje de prueba con ID único {unique_id}",
            "timestamp": f"2025-10-14T10:00:{unique_id % 60}Z"
        }

        with self.client.post("/mensajes", json=message_data, name="POST /mensajes", catch_response=True) as response:
            if response.status_code == 201:
                try:
                    response_json = response.json()
                    message_id = response_json.get('data', {}).get('_id') 
                    
                    if message_id:
                        # Usamos .append() que es atómicamente más seguro que otras operaciones
                        created_message_ids.append(message_id)
                        response.success()
                    else:
                        response.failure(f"201 OK, pero falta '_id' en la respuesta: {response.text}")
                except json.JSONDecodeError:
                    response.failure(f"Respuesta no JSON al crear mensaje: {response.text}")
            else:
                response.failure(f"Fallo al crear mensaje con código {response.status_code}: {response.text}")

    # --- 2. GET /mensajes (Obtener todos) ---
    @task
    def get_all_messages(self):
        """Obtiene la lista de todos los mensajes."""
        self.client.get("/mensajes", name="GET /mensajes")

    # --- 3. GET /mensajes/{id} (Obtener uno por ID) ---
    @task
    def get_message_by_id(self):
        """Obtiene un mensaje por su ID."""
        if not created_message_ids:
            return

        # Seleccionamos y usamos una copia del ID
        message_id = random.choice(created_message_ids)
        
        with self.client.get(f"/mensajes/{message_id}", name="GET /mensajes/[id]", catch_response=True) as response:
            if response.status_code in [200, 404]:
                # 404 es aceptable, significa que fue eliminado justo antes de la consulta
                response.success()
            else:
                response.failure(f"Fallo al obtener mensaje {message_id}: {response.status_code}")


    # --- 4. PUT /mensajes/{id} (Actualizar mensaje) ---
    @task
    def update_message(self):
        """Actualiza el cuerpo de un mensaje existente."""
        if not created_message_ids:
            return

        # Seleccionamos y usamos una copia del ID
        message_id = random.choice(created_message_ids)
        update_data = {
            "cuerpo": f"Cuerpo actualizado por Locust en {random.randint(100, 999)}",
            "editado": True 
        }

        with self.client.put(f"/mensajes/{message_id}", json=update_data, name="PUT /mensajes/[id]", catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            elif response.status_code == 404:
                # El 404 significa que el mensaje ya fue eliminado. 
                # En pruebas de carga concurrente, esto es un escenario válido y no debe contarse como fallo de la API.
                response.success() 
            else:
                response.failure(f"Fallo al actualizar mensaje {message_id}: {response.status_code}")

    # --- 5. DELETE /mensajes/{id} (Eliminar mensaje) ---
    @task
    def delete_message(self):
        """Elimina un mensaje creado."""
        if not created_message_ids:
            return

        # Pop es más rápido que random.choice() y remove() en entornos de alta concurrencia
        message_id = created_message_ids.pop(0) # Eliminar el más antiguo para simular un ciclo de vida FIFO
        
        with self.client.delete(f"/mensajes/{message_id}", name="DELETE /mensajes/[id]", catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            elif response.status_code == 404:
                # El 404 significa que otro proceso ya lo eliminó. También lo marcamos como éxito en la prueba de carga.
                response.success()
            else:
                # Si falla por otra razón, devolvemos el ID a la lista
                created_message_ids.append(message_id) 
                response.failure(f"Fallo al eliminar mensaje {message_id}: {response.status_code}")
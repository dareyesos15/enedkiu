# ./get_data/seguridad_api.py

from dotenv import load_dotenv
import os
import requests
from typing import List, Dict, Union

# Carga las variables de entorno desde el archivo .env
load_dotenv(override=True)

# Obtiene la URL base del servicio de seguridad
SEGURIDAD_SERVICE_URL = os.getenv('SEGURIDAD_SERVICE_URL')


def _make_get_request(endpoint: str) -> List[Dict[str, Union[int, str, float]]]:
    """
    Función de ayuda privada para manejar la lógica común de las peticiones GET.

    Args:
        endpoint: La parte final de la URL (ej: /students).

    Returns:
        Una lista de diccionarios (datos JSON) si la petición es exitosa, 
        o una lista vacía en caso de error.
    """
    if not SEGURIDAD_SERVICE_URL:
        print("ERROR: La variable SEGURIDAD_SERVICE_URL no está configurada en .env.")
        return []

    url = f"{SEGURIDAD_SERVICE_URL}{endpoint}"
    
    try:
        # Realiza la petición GET
        response = requests.get(url, timeout=10) # Se añade un timeout por seguridad

        # Lanza una excepción para códigos de estado 4xx o 5xx
        response.raise_for_status() 

        # Devuelve el cuerpo de la respuesta como un objeto de Python (lista/diccionario)
        return response.json() 

    except requests.exceptions.HTTPError as e:
        # Maneja errores de HTTP (ej. 404 No encontrado, 500 Error del servidor)
        print(f"Error HTTP al llamar a la API de Seguridad ({url}): {e}")
    except requests.exceptions.ConnectionError as e:
        # Maneja errores de conexión (ej. el servicio no está corriendo)
        print(f"Error de conexión con la API de Seguridad ({url}): {e}. Asegúrate de que el servicio esté activo.")
    except requests.exceptions.Timeout as e:
        # Maneja errores de tiempo de espera agotado
        print(f"Tiempo de espera agotado al conectar con la API de Seguridad ({url}): {e}")
    except requests.exceptions.RequestException as e:
        # Maneja cualquier otro error de la librería requests
        print(f"Error desconocido en la petición a la API de Seguridad ({url}): {e}")
        
    return []


def get_all_users() -> List[Dict]:
    """Obtiene la lista de todos los usuarios (admins, estudiantes, profesores) del sistema."""
    print("url: ", SEGURIDAD_SERVICE_URL)
    print("Obteniendo todos los usuarios...")
    return _make_get_request("/users")

def get_all_students() -> List[Dict]:
    """Obtiene la lista de todos los estudiantes del sistema."""
    print("Obteniendo todos los estudiantes...")
    return _make_get_request("/students")

def get_all_professors() -> List[Dict]:
    """Obtiene la lista de todos los profesores del sistema."""
    print("Obteniendo todos los profesores...")
    return _make_get_request("/professors")

def get_user_by_id(user_id: int) -> Dict[str, Union[int, str]] | None:
    """
    Obtiene la información detallada de un usuario específico por ID.

    Args:
        user_id: El ID del usuario.
        
    Returns:
        Un diccionario con los datos del usuario o None si falla.
    """
    print(f"Obteniendo usuario con ID: {user_id}...")
    # Usamos _make_get_request, pero como el endpoint devuelve un solo objeto, 
    # comprobamos si la lista devuelta por _make_get_request es un solo elemento.
    data = _make_get_request(f"/user/{user_id}")
    
    # La API para un solo usuario debería devolver un objeto JSON directamente, 
    # pero nuestra función de ayuda asume una lista. 
    # La ajustamos para que la llamada a la API sea directa para este caso.
    
    url = f"{SEGURIDAD_SERVICE_URL}/user/{user_id}"
    try:
        response = requests.get(url, timeout=10)
        response.raise_for_status()
        return response.json()
    except Exception as e:
        print(f"Error al obtener el usuario {user_id}: {e}")
        return None

# Documentación:
# - Se utiliza una función privada `_make_get_request` para centralizar el manejo 
#   de errores y la lógica de conexión, haciendo el resto del código más limpio.
# - Las funciones `get_all_users`, `get_all_students`, etc., ahora son interfaces 
#   claras que tu `app.py` puede usar.
# - Se incluye el manejo de `requests.exceptions.RequestException` para capturar 
#   errores comunes (conexión, timeouts) y proporcionar mensajes claros.
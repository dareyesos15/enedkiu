# ./requests/cursos_api.py

from dotenv import load_dotenv
import os
import requests
from typing import List, Dict, Union, Any, Optional

# Carga las variables de entorno desde el archivo .env
# Usamos override=True para asegurar que los cambios se reflejen al reiniciar.
load_dotenv(override=True)

# Obtiene la URL base del servicio de Cursos
CURSOS_SERVICE_URL = os.getenv('CURSOS_SERVICE_URL')

# Definimos el tipo de retorno para ser más explícitos
# Usamos 'Any' en la lista porque los objetos pueden ser listas o diccionarios únicos.
ApiResponse = List[Dict[str, Any]] | Dict[str, Any]

def _make_get_request(endpoint: str) -> Optional[ApiResponse]:
    """
    Función de ayuda privada para manejar la lógica común de las peticiones GET.

    Args:
        endpoint: La parte final de la URL (ej: /cursos/{cursoId}).

    Returns:
        Un objeto (lista o diccionario) con los datos JSON si la petición es exitosa, 
        o None en caso de error.
    """
    if not CURSOS_SERVICE_URL:
        print("ERROR: La variable CURSOS_SERVICE_URL no está configurada en .env.")
        return None

    url = f"{CURSOS_SERVICE_URL}{endpoint}"
    
    try:
        # Realiza la petición GET
        response = requests.get(url, timeout=10) # Se añade un timeout por seguridad

        # Lanza una excepción para códigos de estado 4xx o 5xx
        response.raise_for_status() 

        # Devuelve el cuerpo de la respuesta como un objeto de Python (lista/diccionario)
        # Algunos endpoints devuelven una lista, otros un diccionario (ej. un solo curso)
        return response.json() 

    except requests.exceptions.HTTPError as e:
        # Maneja errores de HTTP (ej. 404 No encontrado, 500 Error del servidor)
        print(f"Error HTTP al llamar a la API de cursos ({url}): {e}")
    except requests.exceptions.ConnectionError as e:
        # Maneja errores de conexión (ej. el servicio no está corriendo)
        print(f"Error de conexión con la API de cursos ({url}): {e}. Asegúrate de que el servicio esté activo.")
    except requests.exceptions.Timeout as e:
        # Maneja errores de tiempo de espera agotado
        print(f"Tiempo de espera agotado al conectar con la API de cursos ({url}): {e}")
    except requests.exceptions.RequestException as e:
        # Maneja cualquier otro error de la librería requests
        print(f"Error desconocido en la petición a la API de cursos ({url}): {e}")
        
    return None

# -------------------------------------------------------------------
# FUNCIONES DE CURSOS
# -------------------------------------------------------------------

def get_all_cursos() -> List[Dict]:
    """Obtiene la lista de todos los cursos del sistema."""
    print("Obteniendo todos los cursos...")
    # El endpoint /api/cursos probablemente devuelve una lista de cursos
    data = _make_get_request("/cursos")
    return data if isinstance(data, list) else []

def get_curso_by_id(curso_id: int) -> Optional[Dict]:
    """Obtiene la información de un curso específico por ID."""
    print(f"Obteniendo curso con ID: {curso_id}...")
    # El endpoint /api/cursos/{cursoId} devuelve un único objeto (Diccionario)
    data = _make_get_request(f"/cursos/{curso_id}")
    return data if isinstance(data, dict) else None

def get_tareas_by_curso(curso_id: int) -> List[Dict]:
    """Obtiene la lista de tareas de un curso específico."""
    print(f"Obteniendo tareas para el curso ID: {curso_id}...")
    # Este endpoint devolvería la lista de tareas asociada al curso
    data = _make_get_request(f"/cursos/{curso_id}/tareas")
    return data if isinstance(data, list) else []

def get_cursos_by_profesor(profesor_id: int) -> List[Dict]:
    """Obtiene la lista de cursos asignados a un profesor."""
    print(f"Obteniendo cursos para el profesor ID: {profesor_id}...")
    # Este endpoint es crucial para los reportes de profesores
    data = _make_get_request(f"/cursos/profesor/{profesor_id}")
    return data if isinstance(data, list) else []

def get_cursos_by_estudiante(estudiante_id: int) -> List[Dict]:
    """Obtiene la lista de cursos a los que está inscrito un estudiante."""
    print(f"Obteniendo cursos para el estudiante ID: {estudiante_id}...")
    # Este endpoint es crucial para los reportes de estudiantes
    data = _make_get_request(f"/cursos/estudiante/{estudiante_id}")
    return data if isinstance(data, list) else []

# -------------------------------------------------------------------
# FUNCIONES DE TAREAS Y NOTAS
# -------------------------------------------------------------------

def get_all_tareas() -> List[Dict]:
    """Obtiene la lista de todas las tareas del sistema (REST GET /api/tareas)."""
    print("Obteniendo todas las tareas...")
    data = _make_get_request("/tareas")
    return data if isinstance(data, list) else []

def get_tarea_by_id(tarea_id: int) -> Dict[str, Any] | None:
    """Obtiene la información detallada de una tarea específica por ID."""
    print(f"Obteniendo tarea con ID: {tarea_id}...")
    # El endpoint /api/tareas/{tareaId} devuelve un único objeto (Diccionario)
    data = _make_get_request(f"/tareas/{tarea_id}")
    return data if isinstance(data, dict) else None

def get_notas_by_tarea(tarea_id: int) -> List[Dict]:
    """Obtiene la lista de notas (calificaciones) de una tarea específica."""
    print(f"Obteniendo notas para la tarea ID: {tarea_id}...")
    # Este endpoint devuelve la lista de notas para una tarea
    data = _make_get_request(f"/tareas/{tarea_id}/notas")
    return data if isinstance(data, list) else []

def get_nota_by_id(nota_id: int) -> Optional[Dict]:
    """Obtiene una nota específica por ID (REST GET /api/notas/{id})."""
    print(f"Obteniendo nota con ID: {nota_id}...")
    # Usamos GET en /api/notas/{id}
    data = _make_get_request(f"/notas/{nota_id}")
    return data if isinstance(data, dict) else None

def get_notas_by_estudiante(estudiante_id: int) -> List[Dict]:
    """Obtiene la lista de todas las notas de un estudiante en todas sus asignaturas."""
    print(f"Obteniendo todas las notas para el estudiante ID: {estudiante_id}...")
    # Este endpoint es vital para el reporte general del estudiante
    data = _make_get_request(f"/notas/estudiante/{estudiante_id}")
    return data if isinstance(data, list) else []

# Documentación:
# - El uso de `Optional[ApiResponse]` y la verificación de tipos (`isinstance(data, list/dict)`)
#   es una buena práctica para manejar el hecho de que algunos endpoints devuelven una lista
#   de objetos (ej. todas las tareas) y otros devuelven un único objeto (ej. un curso por ID).
# - Al igual que en `seguridad_api.py`, todas las funciones de cara a la aplicación son interfaces 
#   claras que resumen la lógica de las peticiones HTTP.
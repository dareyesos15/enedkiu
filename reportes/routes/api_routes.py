from flask import Flask, Blueprint, jsonify, request, Response, send_file
import os
import logging

from report_generators.excel_generator import *
from report_generators.pdf_generator import *

# Configurar logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

api = Blueprint('api', __name__, url_prefix="/api")

# Cargar variables de entorno al inicio
API_KEY = os.getenv('API_KEY', 'ingenieria_de_software_2')

"""MIDDLEWARE - API GATEWAY"""
@api.before_request
def requerir_api_key():
    # Endpoints públicos que no requieren API Key
    public_endpoints = ['/api/health', '/api/']
    
    # Si el endpoint es público, permitir el acceso
    if request.path in public_endpoints:
        return None
    
    # Verificar si la API Key está configurada en el servidor
    if not API_KEY or API_KEY == '':
        logger.error("API_KEY no configurada en el servidor")
        return jsonify({
            "error": "API Key no configurada en el servidor"
        }), 500
    
    # Obtener el header Authorization
    auth_header = request.headers.get('Authorization')
    
    # Verificar que el header existe y tiene el formato correcto
    if not auth_header or not auth_header.startswith('Bearer '):
        logger.warning(f"Intento de acceso sin API Key a {request.path}")
        return jsonify({
            "error": "API Key requerida en header Authorization"
        }), 401
    
    # Extraer la API Key del header
    received_api_key = auth_header.replace('Bearer ', '')
    
    # Verificar que la API Key coincida
    if received_api_key != API_KEY:
        logger.warning(f"API Key inválida recibida para {request.path}")
        return jsonify({
            "error": "API Key inválida"
        }), 401
    
    logger.info(f"API Key válida - Acceso permitido a {request.path}")

# -------------------------------------------------------------------
# ENDPOINTS PÚBLICOS
# -------------------------------------------------------------------

@api.route('/health', methods=['GET'])
def health_check():
    return jsonify({
        "status": "OK", 
        "service": "reportes",
        "message": "Microservicio de reportes funcionando correctamente"
    })

# -------------------------------------------------------------------
# REPORTES DE ESTUDIANTE (Visión del Alumno/Administrador)
# -------------------------------------------------------------------

@api.route('/reportes/estudiante/<int:estudiante_id>', methods=['GET'])
def get_reporte_estudiante(estudiante_id: int):
    """
    Endpoint para generar el reporte consolidado de notas de un estudiante.
    
    Parámetros de consulta (Query Params):
    - formato: 'pdf' (predeterminado) o 'excel'
    """
    formato = request.args.get('formato', 'pdf').lower()

    if formato == 'pdf':
        return generar_reporte_estudiante_pdf(estudiante_id)
    elif formato == 'excel':
        return generar_reporte_estudiante_excel(estudiante_id)
    else:
        return jsonify({"error": "Formato no soportado. Usa 'pdf o excel'."}), 400


# -------------------------------------------------------------------
# REPORTES DE CURSO (Visión del Profesor/Administrador)
# -------------------------------------------------------------------

@api.route('/reportes/curso/<int:curso_id>', methods=['GET'])
def get_reporte_curso(curso_id: int):
    """
    Endpoint para generar el reporte de calificaciones detallado de un curso.
    
    Parámetros de consulta:
    - formato: 'excel' (predeterminado) o 'pdf'
    """
    formato = request.args.get('formato', 'excel').lower() 

    if formato == 'pdf':
        return generar_reporte_curso_pdf(curso_id)
    elif formato == 'excel':
        return generar_reporte_curso_excel(curso_id)
    else:
        return jsonify({"error": "Formato no soportado. Usa 'pdf o excel'."}), 400


# -------------------------------------------------------------------
# REPORTES DE TAREAS (Visión del Profesor/Administrador)
# -------------------------------------------------------------------

@api.route('/reportes/tarea/<int:tarea_id>', methods=['GET'])
def get_reporte_tarea(tarea_id: int):
    """
    Endpoint para generar un reporte que lista todas las notas de una tarea específica.
    """
    formato = request.args.get('formato', 'excel').lower()

    if formato == 'excel':
        return generar_reporte_tarea_excel(tarea_id)
    elif formato == 'pdf':
        return generar_reporte_tarea_pdf(tarea_id)
    else:
        return jsonify({"error": "Formato no soportado. Usa 'excel'."}), 400
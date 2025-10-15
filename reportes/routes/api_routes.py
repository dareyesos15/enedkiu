from flask import Blueprint, jsonify, request, Response, send_file

from report_generators.excel_generator import *
from report_generators.pdf_generator import *

api = Blueprint('api', __name__, url_prefix="/api/reportes")

# -------------------------------------------------------------------
# REPORTES DE ESTUDIANTE (Visión del Alumno/Administrador)
# -------------------------------------------------------------------

@api.route('/estudiante/<int:estudiante_id>', methods=['GET'])
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

@api.route('/curso/<int:curso_id>', methods=['GET'])
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

@api.route('/tarea/<int:tarea_id>', methods=['GET'])
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
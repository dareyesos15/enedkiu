# ./routes/api_routes.py

from flask import Blueprint, jsonify, request, Response, send_file
# Importamos las funciones que desarrollaremos para generar los reportes
from report_generators.excel_generator import generar_reporte_curso_excel 
from report_generators.pdf_generator import generar_reporte_estudiante_pdf 
# Nota: Aún no hemos creado estos archivos de generadores, pero los usaremos aquí.

api = Blueprint('api', __name__, url_prefix="/api")

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
        # Llamar a la función que genera el PDF del estudiante
        # (Esta función devolverá el objeto Response con el archivo)
        return generar_reporte_estudiante_pdf(estudiante_id)
        
    # Podemos añadir la lógica para Excel más adelante, si es necesario.
    elif formato == 'excel':
         # Lógica para reporte de estudiante en Excel
        return jsonify({"mensaje": "Reporte de estudiante en Excel no implementado aún."}), 501
    
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
    # Preferimos Excel para reportes de cursos por ser más tabular y manipulable.
    formato = request.args.get('formato', 'excel').lower() 

    if formato == 'excel':
        # Llamar a la función que genera el Excel del curso
        return generar_reporte_curso_excel(curso_id)
        
    elif formato == 'pdf':
        # Lógica para reporte de curso en PDF
        return jsonify({"mensaje": "Reporte de curso en PDF no implementado aún."}), 501
    
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
        # Aquí llamaríamos a la función para generar el reporte de una tarea en Excel
        return jsonify({"mensaje": f"Reporte de Tarea {tarea_id} en Excel listo para implementación."})

    return jsonify({"error": "Formato no soportado. Usa 'pdf o excel'."}), 400


# -------------------------------------------------------------------
# REPORTE ADICIONAL INTERESANTE: Cursos por Profesor
# -------------------------------------------------------------------

@api.route('/listados/profesor/<int:profesor_id>/cursos', methods=['GET'])
def listado_cursos_profesor(profesor_id: int):
    """
    Endpoint de utilidad para listar todos los cursos de un profesor, 
    útil para que el cliente pueda luego pedir reportes de un curso específico.
    """
    # Nota: Este no es un reporte de notas, sino un listado simple en JSON
    # Aquí llamaríamos a una función en el futuro que usa cursos_api.get_cursos_by_profesor()
    return jsonify({"mensaje": f"Listado de cursos para el profesor {profesor_id} listo para implementación."})
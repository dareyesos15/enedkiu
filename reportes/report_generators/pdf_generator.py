# ./report_generators/pdf_generator.py

from flask import send_file, jsonify
import pandas as pd
from io import BytesIO
from weasyprint import HTML, CSS
from typing import Dict, List, Any, Tuple
import datetime

# Importamos las funciones de los módulos de peticiones
from get_data.seguridad_api import get_user_by_id
# Cambiamos la importación: ahora usamos la función que obtiene los cursos del estudiante
from get_data.cursos_api import get_cursos_by_estudiante 

# -------------------------------------------------------------------
# FUNCIÓN PRINCIPAL DE GENERACIÓN DE PDF
# -------------------------------------------------------------------

def _aplanar_datos_notas(cursos_data: List[Dict], estudiante_id: int) -> Tuple[pd.DataFrame, float]:
    """
    Recorre la estructura Curso -> Tarea -> Notas para extraer solo las notas 
    del estudiante y consolidarlas en un DataFrame de Pandas.

    Returns:
        Una tupla con el DataFrame consolidado de notas y el promedio general.
    """
    notas_consolidado = []
    total_calificaciones = []
    
    for curso in cursos_data:
        curso_id = curso.get('id')
        curso_nombre = curso.get('nombre', 'Curso Desconocido')
        
        for tarea in curso.get('tareas', []):
            tarea_id = tarea.get('id')
            tarea_titulo = tarea.get('titulo', 'Tarea sin título')
            
            for nota in tarea.get('notas', []):
                # Filtramos para asegurarnos de que la nota es del estudiante solicitado
                if nota.get('estudianteId') == estudiante_id:
                    
                    # Estructura la fila de datos con todo el contexto necesario
                    notas_consolidado.append({
                        'Curso ID': curso_id,
                        'Curso': curso_nombre,
                        'Tarea ID': tarea_id,
                        'Tarea': tarea_titulo,
                        'Calificación': nota.get('calificacion'),
                        'Mensaje': nota.get('mensaje', '')
                    })
                    total_calificaciones.append(nota.get('calificacion'))
                    
    df = pd.DataFrame(notas_consolidado)
    promedio = round(sum(total_calificaciones) / len(total_calificaciones), 2) if total_calificaciones else 0.0
    
    return df, promedio


def generar_reporte_estudiante_pdf(estudiante_id: int):
    """
    Genera un reporte consolidado de todas las notas de un estudiante en formato PDF.
    """
    print(f"Iniciando generación de reporte PDF (v2) para el estudiante ID: {estudiante_id}")

    # 1. OBTENER INFORMACIÓN BÁSICA DEL ESTUDIANTE
    user_info = get_user_by_id(estudiante_id)
    if not user_info:
        return jsonify({"error": f"Estudiante con ID {estudiante_id} no encontrado."}), 404
        
    nombre_estudiante = str(user_info.get('name', f"Estudiante ID {estudiante_id}"))
    
    # 2. OBTENER DATOS JERÁRQUICOS DE CURSOS DEL ESTUDIANTE
    # Esta es la llamada clave que reemplaza la lógica incorrecta anterior.
    cursos_del_estudiante = get_cursos_by_estudiante(estudiante_id)
    
    if not cursos_del_estudiante:
        # Si el estudiante no tiene cursos, no hay notas
        df_notas = pd.DataFrame()
        promedio_general = 0.0
    else:
        # 3. CONTEXTUALIZAR Y APLANAR LAS NOTAS
        df_notas, promedio_general = _aplanar_datos_notas(cursos_del_estudiante, estudiante_id)
        
    # 4. ESTRUCTURAR DATOS POR CURSO (para la tabla PDF)
    # Agrupamos las notas por el nombre del Curso para la presentación en el PDF
    grouped_notas = df_notas.groupby('Curso') if not df_notas.empty else []
    
    # -------------------------------------------------------------------
    # 5. GENERACIÓN DEL HTML DEL REPORTE
    # -------------------------------------------------------------------

    reporte_html = f"""
    <!DOCTYPE html>
    <html>
    <head>
        <title>Reporte Académico</title>
        <style>
            @page {{ size: A4; margin: 1cm; }}
            body {{ font-family: 'Arial', sans-serif; font-size: 10pt; }}
            header h1 {{ color: #007bff; text-align: center; margin-bottom: 5px; }}
            header p {{ text-align: center; color: #555; margin-top: 0; }}
            .resumen {{ margin: 15px 0; padding: 10px; border: 1px solid #ccc; background-color: #f8f9fa; display: flex; justify-content: space-between; }}
            .resumen-item {{ flex-grow: 1; text-align: center; }}
            table {{ width: 100%; border-collapse: collapse; margin-top: 10px; break-inside: avoid; }} /* break-inside es clave para evitar tablas divididas */
            th, td {{ border: 1px solid #ddd; padding: 8px; text-align: left; }}
            th {{ background-color: #e9ecef; font-weight: bold; }}
            .promedio-final {{ font-size: 14pt; color: {'#28a745' if promedio_general >= 3.0 else '#dc3545'}; font-weight: bold; }}
        </style>
    </head>
    <body>
        <header>
            <h1>REPORTE DE CALIFICACIONES INDIVIDUAL</h1>
            <p>Sistema Educativo Universitario - {datetime.datetime.now().strftime("%d/%m/%Y")}</p>
        </header>

        <h2>Información del Estudiante</h2>
        <div class="resumen">
            <div class="resumen-item"><strong>Nombre:</strong> {nombre_estudiante}</div>
            <div class="resumen-item"><strong>ID:</strong> {estudiante_id}</div>
            <div class="resumen-item"><span class="promedio-final">Promedio General: {promedio_general:.2f}</span></div>
        </div>
    """
    
    # 5.1. Añadir el detalle de las notas por curso
    if not df_notas.empty:
        for curso_name, grupo in grouped_notas:
            reporte_html += f"<h3>{curso_name}</h3>"
            
            # Calculamos el promedio del curso
            promedio_curso = round(grupo['Calificación'].mean(), 2)
            reporte_html += f"<p><strong>Promedio del Curso:</strong> {promedio_curso:.2f}</p>"
            
            # Seleccionamos las columnas a mostrar en el PDF
            tabla_pdf = grupo[['Tarea', 'Calificación', 'Mensaje']]
            tabla_pdf.columns = ['Tarea', 'Calificación', 'Comentarios del Profesor']
            
            # Convertimos la sub-tabla de Pandas a HTML
            reporte_html += tabla_pdf.to_html(classes='table', index=False, justify='left')
            
    else:
         reporte_html += "<h3>El estudiante está inscrito en cursos, pero no hay calificaciones registradas.</h3>"
         
    reporte_html += "</body></html>"


    # 6. GENERAR PDF Y DEVOLVER
    buffer = BytesIO()
    
    try:
        HTML(string=reporte_html).write_pdf(buffer)
        buffer.seek(0)
    except Exception as e:
        print(f"Error al usar WeasyPrint para generar el PDF: {e}")
        return jsonify({"error": "Error interno al generar el archivo PDF."}), 500


    return send_file(
        buffer,
        mimetype='application/pdf',
        as_attachment=True,
        download_name=f'reporte_notas_{nombre_estudiante.replace(" ", "_")}.pdf' 
    )
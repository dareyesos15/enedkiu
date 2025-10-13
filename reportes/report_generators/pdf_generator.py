from flask import send_file, jsonify
import pandas as pd
from io import BytesIO
from weasyprint import HTML, CSS
from typing import Dict, List, Any, Tuple
import datetime

from get_data.seguridad_api import *
from get_data.cursos_api import *

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

def generar_reporte_curso_pdf(curso_id: int):
    """
    Genera un reporte consolidado de calificaciones de un curso específico en formato PDF.

    Args:
        curso_id: El ID del curso a reportar.
    """
    print(f"Iniciando generación de reporte PDF para el curso ID: {curso_id}")

    # 1. OBTENER DATOS DEL CURSO Y VALIDAR
    curso_data = get_curso_by_id(curso_id)

    if not curso_data:
        return jsonify({"error": f"Curso con ID {curso_id} no encontrado o error en el servicio de Cursos."}), 404

    nombre_curso = curso_data.get('nombre', f"Curso ID {curso_id}")
    profesor_id = curso_data.get('profesorId')
    profesor = get_user_by_id(profesor_id) #type: ignore #Pylance advierte que profesor_id puede ser None
    if not profesor: 
        profesor = {"name": "Desconocido"}
        
    tareas_curso = curso_data.get('tareas', []) 
    estudiantes_ids = curso_data.get('estudiantesId', []) 
    
    # 2. OBTENER NOMBRES DE ESTUDIANTES (Del Microservicio de Seguridad)
    estudiantes_map: Dict[int, str] = {}
    for est_id in estudiantes_ids:
        user_info = get_user_by_id(est_id)
        if user_info:
            nombre_completo = user_info.get('name', f"ID_{est_id}") 
            estudiantes_map[est_id] = nombre_completo #type: ignore #Pylance advierte que pueden haber problemas de tipado
        else:
            estudiantes_map[est_id] = f"ID_{est_id}"

    # 3. CONSTRUIR DATOS TABULARES CON PANDAS
    
    # Inicializamos las filas del DataFrame con todos los estudiantes
    reporte_filas = [] 
    for est_id, nombre in estudiantes_map.items():
        reporte_filas.append({
            'ID Estudiante': str(est_id), # Debe ser str para el índice
            'Nombre Completo': nombre,
            'Promedio': 0.0 # Placeholder
        })
    
    df_reporte = pd.DataFrame(reporte_filas)
    df_reporte.set_index('ID Estudiante', inplace=True) 

    notas_por_estudiante: Dict[int, List[float]] = {est_id: [] for est_id in estudiantes_ids}
    
    # Rellenar notas por tarea
    for tarea in tareas_curso:
        tarea_titulo = tarea.get('titulo', f"Tarea ID {tarea.get('id')}")
        notas_tarea = tarea.get('notas', [])
        
        df_reporte[tarea_titulo] = 0.0 # Usamos string vacío para las celdas sin nota
        
        for nota in notas_tarea:
            est_id_int = nota.get('estudianteId')
            est_id_str = str(est_id_int) 
            calificacion = nota.get('calificacion')
            
            if est_id_str in df_reporte.index:
                # Asignamos la nota
                df_reporte.loc[est_id_str, tarea_titulo] = calificacion
                # Guardamos para el promedio
                if est_id_int in notas_por_estudiante:
                    notas_por_estudiante[est_id_int].append(calificacion)

    # Calcular promedios finales
    total_promedio_curso = 0
    conteo_estudiantes_promedio = 0
    for est_id_int, notas_lista in notas_por_estudiante.items():
        est_id_str = str(est_id_int)
        if notas_lista:
            promedio = sum(notas_lista) / len(notas_lista)
            df_reporte.loc[est_id_str, 'Promedio'] = round(promedio, 2)
            total_promedio_curso += promedio
            conteo_estudiantes_promedio += 1
            
    df_reporte.reset_index(inplace=True)
    promedio_general_curso = round(total_promedio_curso / conteo_estudiantes_promedio, 2) if conteo_estudiantes_promedio > 0 else 0.0

    # 4. GENERACIÓN DEL HTML DEL REPORTE

    # Obtenemos la lista de todas las columnas que no son 'ID Estudiante'
    columnas_a_mostrar = [col for col in df_reporte.columns if col not in ['ID Estudiante']] 

    # Quitamos el ID Estudiante del reporte final si queremos un PDF más limpio
    df_html = df_reporte[columnas_a_mostrar]

    # Renombramos 'Promedio' a 'Promedio Final'
    df_html = df_html.rename(columns={'Promedio': 'Promedio Final'})
    
    # Convertimos a HTML
    tabla_html = df_html.to_html(classes='table', index=False)

    reporte_html = f"""
    <!DOCTYPE html>
    <html>
    <head>
        <title>Reporte de Curso</title>
        <style>
            @page {{ size: A4 landscape; margin: 1cm; }} /* Usamos LANDSCAPE para tablas amplias */
            body {{ font-family: 'Arial', sans-serif; font-size: 8pt; }}
            header h1 {{ color: #007bff; text-align: center; margin-bottom: 5px; }}
            header p {{ text-align: center; color: #555; margin-top: 0; }}
            .resumen {{ margin: 10px 0; padding: 10px; border: 1px solid #ccc; background-color: #f8f9fa; display: flex; }}
            .resumen-item {{ flex-grow: 1; margin-right: 15px; }}
            table {{ width: 100%; border-collapse: collapse; margin-top: 10px; }}
            th, td {{ border: 1px solid #ddd; padding: 4px 6px; text-align: left; }}
            th {{ background-color: #e9ecef; font-weight: bold; }}
            .promedio-final-curso {{ font-size: 12pt; color: #28a745; font-weight: bold; }}
        </style>
    </head>
    <body>
        <header>
            <h1>REPORTE CONSOLIDADO DE CALIFICACIONES</h1>
            <p>{nombre_curso} | Profesor: {profesor["name"]} | Fecha: {datetime.datetime.now().strftime("%d/%m/%Y")}</p> 
        </header>

        <div class="resumen">
            <div class="resumen-item"><strong>Total Alumnos:</strong> {len(estudiantes_ids)}</div>
            <div class="resumen-item"><strong>Tareas Registradas:</strong> {len(tareas_curso)}</div>
            <div class="resumen-item"><span class="promedio-final-curso">Promedio General del Curso: {promedio_general_curso:.2f}</span></div>
        </div>
        
        <h2>Detalle de Calificaciones</h2>
        
        {tabla_html}

    </body></html>
    """ 
    
    # 5. GENERAR PDF Y DEVOLVER
    buffer = BytesIO()
    
    try:
        HTML(string=reporte_html).write_pdf(buffer, stylesheets=[CSS(string='@page { size: A4 landscape; }')])
        buffer.seek(0)
    except Exception as e:
        print(f"Error al usar WeasyPrint para generar el PDF del curso: {e}")
        return jsonify({"error": "Error interno al generar el archivo PDF."}), 500


    return send_file(
        buffer,
        mimetype='application/pdf',
        as_attachment=True,
        download_name=f'reporte_curso_{nombre_curso.replace(" ", "_")}.pdf'
    )

def generar_reporte_tarea_pdf(tarea_id: int):
    """
    Genera un reporte que lista todas las notas y comentarios de una tarea específica en PDF.

    Args:
        tarea_id: El ID de la tarea a reportar.
    """
    print(f"Iniciando generación de reporte PDF para la Tarea ID: {tarea_id}")

    # 1. OBTENER INFORMACIÓN DE LA TAREA Y SUS NOTAS
    tarea_info = get_tarea_by_id(tarea_id)
    if not tarea_info:
         return jsonify({"error": f"Tarea con ID {tarea_id} no encontrada."}), 404
         
    tarea_titulo = tarea_info.get('titulo', f"Tarea ID {tarea_id}")
    tarea_descripcion = tarea_info.get('descripcion', 'Sin descripción')
    tarea_fecha = tarea_info.get('fechaEntrega', 'N/A').split('T')[0]
    
    # Obtenemos solo la lista plana de notas
    notas_tarea_raw = get_notas_by_tarea(tarea_id)

    if not notas_tarea_raw:
        return jsonify({"error": f"La tarea '{tarea_titulo}' no tiene notas registradas."}), 404

    df_reporte = pd.DataFrame(notas_tarea_raw)
    
    # 2. OBTENER NOMBRES DE ESTUDIANTES (Agregación)
    
    estudiantes_ids = df_reporte['estudianteId'].unique()
    estudiantes_map: Dict[int, str] = {}
    
    for est_id in estudiantes_ids:
        user_info = get_user_by_id(int(est_id)) 
        if user_info:
            nombre_completo = user_info.get('name', f"ID_{est_id}") 
            estudiantes_map[int(est_id)] = nombre_completo #type: ignore #Pylance advierte que puede haber error de tipado
    
    # 3. TRANSFORMAR Y LIMPIAR DATOS
    
    # Mapeamos los IDs de estudiante a sus nombres
    df_reporte['Estudiante ID Num'] = df_reporte['estudianteId'].astype(int)
    df_reporte['Estudiante'] = df_reporte['Estudiante ID Num'].map(lambda x: estudiantes_map.get(x, f"ID_{x}"))

    # 4. SELECCIÓN DE COLUMNAS FINALES Y RENOMBRAMIENTO
    
    df_reporte = df_reporte[['Estudiante', 'calificacion', 'mensaje']]
                             
    df_reporte.columns = ['Estudiante', 'Calificación', 'Comentarios del Profesor']

    # Opcional: Ordenar por calificación descendente
    df_reporte = df_reporte.sort_values(by='Calificación', ascending=False)
    
    # Cálculos para el resumen
    promedio_tarea = round(df_reporte['Calificación'].mean(), 2)
    total_notas = len(df_reporte)
    
    # 5. GENERACIÓN DEL HTML DEL REPORTE

    tabla_html = df_reporte.to_html(classes='table', index=False, justify='left')

    reporte_html = f"""
    <!DOCTYPE html>
    <html>
    <head>
        <title>Reporte de Tarea</title>
        <style>
            @page {{ size: A4; margin: 1cm; }}
            body {{ font-family: 'Arial', sans-serif; font-size: 10pt; }}
            header h1 {{ color: #dc3545; text-align: center; margin-bottom: 5px; }}
            header p {{ text-align: center; color: #555; margin-top: 0; }}
            .resumen {{ margin: 15px 0; padding: 10px; border: 1px solid #ccc; background-color: #f8f9fa; display: flex; justify-content: space-between; }}
            .resumen-item {{ flex-grow: 1; text-align: center; }}
            table {{ width: 100%; border-collapse: collapse; margin-top: 10px; }}
            th, td {{ border: 1px solid #ddd; padding: 8px; text-align: left; }}
            th {{ background-color: #e9ecef; font-weight: bold; }}
            .promedio-final {{ font-size: 12pt; color: #007bff; font-weight: bold; }}
        </style>
    </head>
    <body>
        <header>
            <h1>REPORTE DE CALIFICACIONES POR TAREA</h1>
            <p>Tarea ID: {tarea_id} | Fecha de Entrega: {tarea_fecha} | Generado: {datetime.datetime.now().strftime("%d/%m/%Y")}</p>
        </header>

        <h2>{tarea_titulo}</h2>
        <p><strong>Descripción:</strong> {tarea_descripcion}</p>

        <div class="resumen">
            <div class="resumen-item"><strong>Total de Notas:</strong> {total_notas}</div>
            <div class="resumen-item"><span class="promedio-final">Promedio de la Tarea: {promedio_tarea:.2f}</span></div>
        </div>

        <h3>Detalle de Calificaciones</h3>
        {tabla_html}

    </body></html>
    """
    
    # 6. GENERAR PDF Y DEVOLVER
    buffer = BytesIO()
    nombre_archivo = f'reporte_tarea_{tarea_titulo.replace(" ", "_").replace(":", "")}'
    
    try:
        HTML(string=reporte_html).write_pdf(buffer)
        buffer.seek(0)
    except Exception as e:
        print(f"Error al usar WeasyPrint para generar el PDF de la tarea: {e}")
        return jsonify({"error": "Error interno al generar el archivo PDF."}), 500


    return send_file(
        buffer,
        mimetype='application/pdf',
        as_attachment=True,
        download_name=f'{nombre_archivo}.pdf'
    )
from flask import send_file, jsonify
import pandas as pd
from io import BytesIO
from typing import Dict, List, Any, Tuple

from get_data.cursos_api import *
from get_data.seguridad_api import *

def _aplanar_datos_notas_estudiante(cursos_data: List[Dict], estudiante_id: int) -> Tuple[pd.DataFrame, float]:
    """
    Recorre la estructura Curso -> Tarea -> Notas para extraer solo las notas 
    del estudiante y consolidarlas en un DataFrame de Pandas.
    Esta función es idéntica a la que creamos para el PDF, pero vive en el contexto del Excel.
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
                if nota.get('estudianteId') == estudiante_id:
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

def generar_reporte_estudiante_excel(estudiante_id: int):
    """
    Genera un reporte consolidado de todas las notas de un estudiante en formato Excel.

    Args:
        estudiante_id: El ID del estudiante a reportar.
    """
    print(f"Iniciando generación de reporte Excel para el estudiante ID: {estudiante_id}")

    # 1. OBTENER INFORMACIÓN BÁSICA DEL ESTUDIANTE Y CURSOS
    user_info = get_user_by_id(estudiante_id)
    if not user_info:
        return jsonify({"error": f"Estudiante con ID {estudiante_id} no encontrado."}), 404
        
    nombre_estudiante = user_info.get('name', f"Estudiante ID {estudiante_id}")
    cursos_del_estudiante = get_cursos_by_estudiante(estudiante_id)
    
    if not cursos_del_estudiante:
        return jsonify({"mensaje": f"El estudiante {nombre_estudiante} no está inscrito en ningún curso con notas."}), 200

    # 2. APLANAR Y CONSOLIDAR NOTAS
    df_reporte, promedio_general = _aplanar_datos_notas_estudiante(cursos_del_estudiante, estudiante_id)
    
    if df_reporte.empty:
        return jsonify({"mensaje": f"El estudiante {nombre_estudiante} no tiene calificaciones registradas."}), 200

    # 3. RENOMBRAR Y ORDENAR COLUMNAS
    df_reporte = df_reporte[['Curso', 'Tarea', 'Calificación', 'Mensaje', 'Curso ID', 'Tarea ID']]
    df_reporte.columns = ['Curso', 'Tarea', 'Calificación', 'Comentarios del Profesor', 'ID Curso', 'ID Tarea']
    
    # Ordenar por Curso y luego por Tarea (para mejor visualización)
    df_reporte = df_reporte.sort_values(by=['Curso', 'ID Tarea'], ascending=[True, True])

    # 4. GENERAR ARCHIVO EXCEL EN MEMORIA (BytesIO)
    buffer = BytesIO()
    nombre_archivo = f'reporte_notas_{nombre_estudiante.replace(" ", "_")}' #type: ignore #Pylance advierte que se puede recibir un int sin la propiedad replace
    
    try:
        with pd.ExcelWriter(buffer, engine='openpyxl') as writer:
            # Hoja de Notas Detalladas
            df_reporte.to_excel(writer, sheet_name='Detalle de Notas', index=False)
            
            # Hoja de Resumen (Métricas del Estudiante)
            df_resumen = pd.DataFrame([
                {"Métrica": "Estudiante", "Valor": nombre_estudiante},
                {"Métrica": "Total Cursos con Notas", "Valor": df_reporte['Curso'].nunique()},
                {"Métrica": "Total de Notas", "Valor": len(df_reporte)},
                {"Métrica": "Promedio General", "Valor": promedio_general}
            ])
            df_resumen.to_excel(writer, sheet_name='Resumen', index=False)
            
        buffer.seek(0)
    except Exception as e:
        print(f"Error al escribir el archivo Excel para Estudiante {estudiante_id}: {e}")
        return jsonify({"error": "Error interno al generar el archivo Excel."}), 500

    # 5. DEVOLVER EL ARCHIVO
    return send_file(
        buffer,
        mimetype='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        as_attachment=True,
        download_name=f'{nombre_archivo}.xlsx'
    )

def generar_reporte_curso_excel(curso_id: int):
    """
    Genera un reporte consolidado de calificaciones de un curso específico en formato Excel.

    Args:
        curso_id: El ID del curso a reportar.
        
    Returns:
        Un objeto Response de Flask con el archivo Excel para descarga.
    """
    print(f"Iniciando generación de reporte Excel para el curso ID: {curso_id}")

    # 1. OBTENER DATOS DEL CURSO Y VALIDAR
    curso_data = get_curso_by_id(curso_id)

    if not curso_data:
        return jsonify({"error": f"Curso con ID {curso_id} no encontrado o error en el servicio de Cursos."}), 404

    # 2. PREPARAR ESTRUCTURAS DE DATOS

    # Lista para almacenar las filas finales del reporte
    reporte_filas = [] 
    # Extraemos solo las tareas del curso
    tareas_curso = curso_data.get('tareas', []) 
    # Los IDs de los estudiantes inscritos en el curso
    estudiantes_ids = curso_data.get('estudiantesId', []) 
    
    # 3. OBTENER NOMBRES DE ESTUDIANTES (Del Microservicio de Seguridad)
    estudiantes_map: Dict[int, str] = {}
    
    for est_id in estudiantes_ids:
        # Aquí estamos asumiendo que el ID del estudiante es el mismo que el ID del usuario
        user_info = get_user_by_id(est_id)
        if user_info:
            # Usamos el nombre del usuario si existe, si no, usamos el ID como fallback
            nombre_completo = user_info.get('name', f"ID_{est_id}") 
            estudiantes_map[est_id] = nombre_completo #type: ignore # Pylance advierte que pueden haber errores de tipado
        else:
            estudiantes_map[est_id] = f"ID_{est_id}" # Fallback si falla la petición

    # 4. TRANSFORMAR Y AGREGAR DATOS (Procesamiento ETL)
    for est_id, nombre in estudiantes_map.items():
        reporte_filas.append({
            'ID Estudiante': est_id,
            'Nombre Completo': nombre,
            'Promedio': 0.0 # Placeholder
        })

    # Convertimos a DataFrame para facilitar la manipulación de columnas.
    df_reporte = pd.DataFrame(reporte_filas)
    df_reporte.set_index('ID Estudiante', inplace=True) # Usamos el ID como índice temporal

    # 5. POBLAR COLUMNAS DE NOTAS POR TAREA
    # Mantenemos un registro de las notas para el cálculo del promedio
    notas_por_estudiante: Dict[int, List[float]] = {est_id: [] for est_id in estudiantes_ids}
    
    for tarea in tareas_curso:
        tarea_titulo = tarea.get('titulo', f"Tarea ID {tarea.get('id')}")
        notas_tarea = tarea.get('notas', [])
        
        # Agregamos una nueva columna al DataFrame para esta tarea
        df_reporte[tarea_titulo] = 0.0 # Inicializamos la columna con 0.0
        
        for nota in notas_tarea:
            est_id = nota.get('estudianteId')
            calificacion = nota.get('calificacion') 
            
            if est_id in df_reporte.index:
                # Asignamos la nota a la celda correspondiente
                df_reporte.loc[est_id, tarea_titulo] = calificacion 
                # Guardamos la nota para el cálculo del promedio
                notas_por_estudiante[est_id].append(calificacion)

    # 6. CALCULAR PROMEDIOS FINALES
    for est_id, notas_lista in notas_por_estudiante.items():
        if notas_lista:
            # Calcula el promedio de las notas y lo asigna
            promedio = sum(notas_lista) / len(notas_lista)
            df_reporte.loc[est_id, 'Promedio'] = round(promedio, 2)
    
    # Reiniciamos el índice para que 'ID Estudiante' vuelva a ser una columna
    df_reporte.reset_index(inplace=True) 

    # 7. GENERAR ARCHIVO EXCEL EN MEMORIA (BytesIO)
    buffer = BytesIO()
    nombre_curso = curso_data.get('nombre', 'Curso Desconocido').replace(' ', '_')
    
    try:
        with pd.ExcelWriter(buffer, engine='openpyxl') as writer:
            # Escribimos el reporte principal
            df_reporte.to_excel(writer, sheet_name='Calificaciones', index=False)
            
            # Opcional: Agregar una hoja de resumen del curso
            df_resumen = pd.DataFrame([
                {"Métrica": "Curso", "Valor": curso_data.get('nombre')},
                {"Métrica": "Profesor ID", "Valor": curso_data.get('profesorId')},
                {"Métrica": "Total Alumnos", "Valor": len(estudiantes_ids)},
                {"Métrica": "Promedio General del Curso", "Valor": round(df_reporte['Promedio'].mean(), 2)}
            ])
            df_resumen.to_excel(writer, sheet_name='Resumen', index=False)
            
        buffer.seek(0)
    except Exception as e:
        print(f"Error al escribir el archivo Excel: {e}")
        return jsonify({"error": "Error interno al generar el archivo Excel."}), 500

    # 8. DEVOLVER EL ARCHIVO
    return send_file(
        buffer,
        mimetype='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        as_attachment=True,
        download_name=f'reporte_notas_{nombre_curso}.xlsx'
    )

def generar_reporte_tarea_excel(tarea_id: int):
    """
    Genera un reporte que lista todas las notas y comentarios de una tarea específica en Excel.
    """
    print(f"Iniciando generación de reporte Excel para la Tarea ID: {tarea_id}")

    # 1. OBTENER INFORMACIÓN DE LA TAREA Y SUS NOTAS
    
    # Obtenemos el título de la tarea (nueva llamada)
    tarea_info = get_tarea_by_id(tarea_id)
    if not tarea_info:
         return jsonify({"error": f"Tarea con ID {tarea_id} no encontrada."}), 404
         
    tarea_titulo = tarea_info.get('titulo', f"Tarea ID {tarea_id}")
    tarea_descripcion = tarea_info.get('descripcion', 'Sin descripción')
    
    # Obtenemos solo la lista plana de notas
    notas_tarea_raw = get_notas_by_tarea(tarea_id)

    if not notas_tarea_raw:
        return jsonify({"error": f"La tarea '{tarea_titulo}' no tiene notas registradas."}), 404

    df_reporte = pd.DataFrame(notas_tarea_raw)
    
    # 2. OBTENER NOMBRES DE ESTUDIANTES (Agregación)
    
    estudiantes_ids = df_reporte['estudianteId'].unique()
    estudiantes_map: Dict[int, str] = {}
    
    for est_id in estudiantes_ids:
        # Aseguramos el cast a int, aunque Pandas a menudo devuelve numpy.int64
        user_info = get_user_by_id(int(est_id)) 
        if user_info:
            nombre_completo = user_info.get('name', f"ID_{est_id}") 
            estudiantes_map[int(est_id)] = nombre_completo #type: ignore #Pylance advierte que pueden haber problemas de tipado

    # 3. TRANSFORMAR Y LIMPIAR DATOS
    
    # Mapeamos los IDs de estudiante a sus nombres
    # Renombramos la columna 'estudianteId' a un tipo numérico temporal para mapear
    df_reporte['Estudiante ID Num'] = df_reporte['estudianteId'].astype(int)
    df_reporte['Estudiante'] = df_reporte['Estudiante ID Num'].map(lambda x: estudiantes_map.get(x, f"ID_{x}"))

    # Agregamos los campos de la tarea
    df_reporte['Tarea'] = tarea_titulo
    df_reporte['Descripción de Tarea'] = tarea_descripcion
    
    # 4. SELECCIÓN DE COLUMNAS FINALES Y RENOMBRAMIENTO
    
    # Cambiamos el orden para que la información de la tarea sea visible
    df_reporte = df_reporte[['Tarea', 'Descripción de Tarea', 'Estudiante', 
                             'calificacion', 'mensaje']]
                             
    df_reporte.columns = ['Tarea', 'Descripción', 'Estudiante', 
                          'Calificación', 'Comentarios del Profesor']

    # Opcional: Ordenar por calificación descendente
    df_reporte = df_reporte.sort_values(by='Calificación', ascending=False)
    
    # 5. GENERAR ARCHIVO EXCEL EN MEMORIA (BytesIO)
    
    buffer = BytesIO()
    # Usamos el título de la tarea para el nombre del archivo
    nombre_archivo = f'reporte_{tarea_titulo.replace(" ", "_").replace(":", "")}'
    
    try:
        with pd.ExcelWriter(buffer, engine='openpyxl') as writer:
            df_reporte.to_excel(writer, sheet_name='Notas', index=False)
            
            # Ajuste de la hoja de Resumen
            df_resumen = pd.DataFrame([
                {"Métrica": "Tarea", "Valor": tarea_titulo},
                {"Métrica": "Total Notas Registradas", "Valor": len(df_reporte)},
                {"Métrica": "Promedio de la Tarea", "Valor": round(df_reporte['Calificación'].mean(), 2)}
            ])
            df_resumen.to_excel(writer, sheet_name='Resumen', index=False)
            
        buffer.seek(0)
    except Exception as e:
        print(f"Error al escribir el archivo Excel para Tarea {tarea_id}: {e}")
        return jsonify({"error": "Error interno al generar el archivo Excel."}), 500


    # 6. DEVOLVER EL ARCHIVO
    
    return send_file(
        buffer,
        mimetype='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        as_attachment=True,
        download_name=f'{nombre_archivo}.xlsx'
    )
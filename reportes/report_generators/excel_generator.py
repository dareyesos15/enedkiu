# ./report_generators/excel_generator.py

from flask import send_file, jsonify
import pandas as pd
from io import BytesIO
from typing import Dict, List, Any

# Importamos las funciones de los módulos de peticiones
from get_data.cursos_api import get_curso_by_id
from get_data.seguridad_api import get_user_by_id # Usaremos esta para obtener nombres

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
    # El API de cursos devuelve el objeto curso con una lista anidada de 'tareas', 
    # y cada tarea tiene una lista anidada de 'notas'.
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
    
    # Mapeo: {id_estudiante: "Nombre Completo"}
    # Esto evita hacer peticiones repetidas al API de seguridad y centraliza la información.
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
    
    # Inicializamos las filas del reporte con los estudiantes.
    # La columna 'Promedio' será calculada al final.
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
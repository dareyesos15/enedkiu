from flask import Flask, Blueprint, jsonify, request, Response
from database.mongo_connection import db
from bson import json_util
from bson.objectid import ObjectId
import os
import logging

# Configurar logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

api = Blueprint('api', __name__, url_prefix='/api')

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

# Agregar endpoint de health check
@api.route('/health', methods=['GET'])
def health_check():
    return jsonify({
        "status": "OK", 
        "service": "mensajes",
        "message": "Microservicio de mensajes funcionando correctamente"
    })

@api.route('/mensajes', methods=['GET'])
def mensajes_todos():
    mensajes_cursor = db.find()
    mensajes_list = list(mensajes_cursor)

    # Si la lista está vacía
    if not mensajes_list:
        return jsonify({
            'mensaje': 'No hay mensajes en la base de datos.'
        }), 404

    # Usar json_util para serializar correctamente los datos de BSON (incluyendo ObjectId)
    json_response = json_util.dumps(mensajes_list)
    return Response(json_response, status=200, mimetype='application/json')

@api.route('/mensajes/<id>', methods=['GET'])
def mensaje_uno(id):
    try:
        # Convertir el string del id a un ObjectId de MongoDB
        object_id = ObjectId(id)
    except Exception:
        return jsonify({'mensaje': 'El ID proporcionado no es válido.'}), 400

    # Intentar actualizar el documento
    mensaje = db.find_one({"_id": object_id})
    print(mensaje)

    if mensaje:
        json_response = json_util.dumps(mensaje)
        return Response(json_response, status=200, mimetype='application/json')

    else:
        return jsonify({'mensaje': 'Mensaje no encontrado.'}), 404

@api.route('/mensajes/remitente/<int:remitente_id>', methods=['GET'])
def mensajes_por_remitente(remitente_id):
    query = {
        'remitente_id': remitente_id
    }

    mensajes_cursor = db.find(query)
    mensajes_list = list(mensajes_cursor)

    # Si la lista está vacía
    if not mensajes_list:
        return jsonify({
            'mensaje': 'No se han enviado mensajes.'
        }), 404

    json_response = json_util.dumps(mensajes_list)
    return Response(json_response,status=200, mimetype='application/json')

@api.route('/mensajes/receptor/<int:receptor_id>', methods=['GET'])
def mensajes_por_receptor(receptor_id):
    query = {
        'receptores_id': receptor_id
    }

    mensajes_cursor = db.find(query)
    mensajes_list = list(mensajes_cursor)

    # Si la lista está vacía
    if not mensajes_list:
        return jsonify({
            'mensaje': 'No se han recibido mensajes.'
        }), 404

    json_response = json_util.dumps(mensajes_list)
    return Response(json_response, status=200, mimetype='application/json')

"""METODOS POST"""
@api.route('/mensajes', methods=['POST'])
def mensaje_guardar():
    mensaje = request.get_json()
    
    if not mensaje:
        return jsonify({'mensaje': 'No se recibió ningún dato en el cuerpo de la petición.'}), 400

    try:
        result = db.insert_one(mensaje)
        
        # Devolvemos el ID del documento insertado como confirmación
        return jsonify({
            "mensaje": "Mensaje guardado con éxito",
            "id": str(result.inserted_id)
        }), 201
    
    except Exception as e:
        return jsonify({
            'mensaje': 'Error al guardar el mensaje',
            'error': str(e)
        }), 500

"""METODO PUT"""
@api.route('/mensajes/<id>', methods=['PUT'])
def mensaje_editar(id):
    try:
        # Convertir el string del id a un ObjectId de MongoDB
        object_id = ObjectId(id)
    except Exception:
        return jsonify({'mensaje': 'El ID proporcionado no es válido.'}), 400

    # Obtener los datos a actualizar desde el cuerpo de la petición
    datos_actualizados = request.get_json()
    if not datos_actualizados:
        return jsonify({'mensaje': 'No se recibieron datos para actualizar.'}), 400

    # Intentar actualizar el documento
    result = db.update_one(
        {'_id': object_id},
        {'$set': datos_actualizados}
    )

    if result.modified_count == 1:
        return jsonify({'mensaje': 'Mensaje actualizado con éxito.'}), 200

    else:
        return jsonify({'mensaje': 'Mensaje no encontrado.'}), 404
    
"""METODO DELETE"""
@api.route('/mensajes/<id>', methods=['DELETE'])
def mensaje_eliminar(id):
    try:
        # Convertir el string del id a un ObjectId de MongoDB
        object_id = ObjectId(id)
    except Exception:
        return jsonify({'mensaje': 'El ID proporcionado no es válido.'}), 400

    # Intentar eliminar el documento que coincida con el _id
    result = db.delete_one({'_id': object_id})

    # Verificar si se eliminó algún documento
    if result.deleted_count == 1:
        return jsonify({'mensaje': 'Mensaje eliminado con éxito.'}), 200
    else:
        return jsonify({'mensaje': 'Mensaje no encontrado.'}), 404
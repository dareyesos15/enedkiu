from flask import Flask, Blueprint, jsonify, request, Response
from database.mongo_connection import db
from bson import json_util

api = Blueprint('api', __name__)

"""METODOS GET"""
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
        db.insert_one(mensaje)
        
        # Devolvemos el ID del documento insertado como confirmación
        mensaje['_id'] = str(mensaje['_id'])
        return jsonify({
            "mensaje": "Mensaje guardado con éxito",
            "data": mensaje
        }), 201
    
    except Exception as e:
        return jsonify({
            'mensaje': 'Error al guardar el mensaje',
            'error': str(e)
        }), 500

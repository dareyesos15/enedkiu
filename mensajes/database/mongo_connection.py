import os
from pymongo import MongoClient

def db_connection(database_url, database_name, collection_name):
    try:
        client = MongoClient(database_url)
        db = client[database_name]
        collection = db[collection_name]
        print("Conexión a la base de datos exitosa.")
        return collection
    except Exception as e:
        print(f"Error al conectar a la base de datos: {e}")
        raise e

DATABASE_URL = os.getenv('DATABASE_URL')
DATABASE_NAME = os.getenv('DATABASE_NAME')
DATABASE_COLLECTION = os.getenv('DATABASE_COLLECTION')

# Inicializa la conexión a la base de datos aquí
db = db_connection(DATABASE_URL, DATABASE_NAME, DATABASE_COLLECTION)

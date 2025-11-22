from flask import Flask, jsonify
from routes.api_routes import api 

app = Flask(__name__)

# ---- Importar rutas ----
app.register_blueprint(api) 

@app.route('/', methods=['GET'])
def home():
    """Ruta simple para verificar que el servicio está activo."""
    return jsonify({
        "status": "OK",
        "servicio": "Microservicio de Reportes Educativos",
        "rutas_api": "/reportes/...",
        "lenguaje": "Python con Flask"
    })
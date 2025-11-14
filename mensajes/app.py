from flask import Flask
from flask_cors import CORS
import dotenv

# Cargar variables de entorno ANTES de cualquier otra importación local
dotenv.load_dotenv()

# Archivos locales
from routes.api_routes import api

# Flask
app = Flask(__name__)
app.register_blueprint(api, url_prefix='/api')

# solo para desarrollo NO SEGURO
CORS(app)

if __name__ == '__main__':
    app.run(debug=True)
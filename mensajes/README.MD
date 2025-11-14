# Microservicios de mensajería de la aplicación

## Tecnologías
**Python-Flask + MongoDB**

## Dependencias
- **Python3:** https://www.python.org/downloads/
- **MongoDB Compass:** https://www.mongodb.com/try/download/atlas-kubernetes-operator

## levantar el microservicio en servidor local
- **Crear entorno virtual de python (recomendado):** `python -m venv venv`
- **Iniciar entorno virtual de python** `./venv/Scripts/activate` ó `source .venv/bin/activate`
- **Instalar dependencias de python** `pip install -r requeriments.txt`

## Url de Servidor local de desarrollo por defecto
`http://localhost:5000`

## Endpoints
- /api/mensajes (REST)
- /api/mensajes/remitente/{remitente_id}
- /api/mensajes/receptor/{receptor_id}
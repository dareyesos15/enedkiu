# Microservicio de reportes de enedkiu

## Tecnologías

**Python-Flask**

## Dependencias
Ser recomienda crear un entorno virtual de python y utilizar el comando `pip install -r requirements.txt` para instalar las dependencias de python necesarias.

* Se debe instalar gtk3-runtime para poder generar los reportes pdf: https://doc.courtbouillon.org/weasyprint/v54.3/first_steps.html

## levantar el microservicio en servidor local
`flask --app app run --port 5001`

## Url de servidor local de desarrollo  
`http://localhost:5001`

## Endpoints
- /api/reportes/estudiante/{estudiante_id}
- /api/reportes/curso/{curso_id}
- /api/reportes/tarea/{tarea_id}

En cualquiera de los casos incluir al final la variable ?formato= con los valores pdf o excel. Ejemplo: http://localhost:5001/api/reportes/curso/1?formato=pdf


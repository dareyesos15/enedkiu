describe('Test de funcionalidad para el microservicio "cursos"', () => {
  const API_URL = 'http://localhost:8080/api/cursos'
  let createdCursoId;
  let createdCursoNombre;

  it('Mostrar todos los cursos', () => {
    cy.request('GET', API_URL).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.be.an('array')
        })
  })

  it('Debe crear un curso (POST)', () => {
        const newCurso = {  
            "nombre": "Prueba Func",
            "estudiantesId": [ 8, 9],
            "profesorId": 2,
            "asignaturaId": 1,        }

        cy.request('POST', API_URL, newCurso).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.property('id')
            expect(response.body).to.have.property('nombre')
            createdCursoId = response.body.id
            createdCursoNombre = response.body.nombre
        })
    })

    it('Debe mostrar el curso creado (GET /{cursoId})', () => {
        cy.request('GET', API_URL+`/${createdCursoId}`).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.property('id', createdCursoId)
            expect(response.body).to.have.property('nombre', createdCursoNombre)
        })
    })

    it('Debe modificar el usuario creado (PUT)', () => {
        const modifyCurso = {  
            "nombre": "Prueba Func modificado",
            "estudiantesId": [ 8, 9],
            "profesorId": 4,
        }

        cy.request('PUT', API_URL+`/${createdCursoId}`, modifyCurso).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.property('id')
            expect(response.body.nombre).to.eq(modifyCurso.nombre)
        })
    })

    it('Debe eliminar el curso creado (DELETE)', () => {
        cy.request('DELETE', API_URL+`/${createdCursoId}`).then((response) => {
            expect(response.status).to.eq(200)
        })
    })
})
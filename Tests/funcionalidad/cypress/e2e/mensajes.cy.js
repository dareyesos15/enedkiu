describe('Test de funcionalidad para el microservicio "mensajes"', () => {
  const API_URL = 'http://localhost:5000/api'
  let createdMensajeId;

  it('Mostrar todos los mensajes', () => {
    cy.request('GET', `${API_URL}/mensajes`).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.be.an('array')
        })
  })

  it('Debe crear un mensaje (POST)', () => {
        const newMensaje = {  
            "titulo": "mensaje de prueba funcional",
            "mensaje": "mensaje de prueba funcional :)",
            "remitente_id": 1,
            "receptores": "estudiantes"       
        }

        cy.request('POST', `${API_URL}/mensajes`, newMensaje).then((response) => {
            expect(response.status).to.eq(201)
            expect(response.body).to.have.property('data')
            expect(response.body.mensaje).to.eq("Mensaje guardado con éxito")
            createdMensajeId = response.body.data._id
        })
    })

    it('Debe mostrar el mensaje creado (GET /{mensaje_id})', () => {
        cy.request('GET', `${API_URL}/mensajes/${createdMensajeId}`).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.property('_id')
            expect(response.body).to.have.property('titulo')
        })
    })

    it('Debe modificar el mensaje creado (PUT)', () => {
        const modifyMensaje = {  
            "titulo": "mensaje de prueba funcional actualizado",
            "mensaje": "mensaje de prueba funcional :) actualizado",
            "remitente_id": 3,
            "receptores": "estudiantes"       
        }

        cy.request('PUT', `${API_URL}/mensajes/${createdMensajeId}`, modifyMensaje).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body.mensaje).to.eq("Mensaje actualizado con éxito.")
        })
    })

    it('Debe eliminar el mensaje creado (DELETE)', () => {
        cy.request('DELETE', API_URL+`/mensajes/${createdMensajeId}`).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body.mensaje).to.eq("Mensaje eliminado con éxito.")
        })
    })
})
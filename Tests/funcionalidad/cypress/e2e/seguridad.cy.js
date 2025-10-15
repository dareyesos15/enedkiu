describe('Test de funcionalidad para el microservicio "seguridad"', () => {
  const API_URL = 'http://localhost:8000/api'
  let createdUserId;

  it('Mostrar todos los usuarios', () => {
    cy.request('GET', `${API_URL}/users`).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.be.an('array')
        })
  })

  it('Debe crear un usuario (POST)', () => {
        const newUser = {  
            "name": "Usuario de prueba",
            "email": "usuario_prueba@example.com",
            "password": "contrasenasegura",       
          }

        cy.request('POST', `${API_URL}/create_user`, newUser).then((response) => {
            expect(response.status).to.eq(201)
            expect(response.body).to.have.property('user')
            expect(response.body.message).to.eq("Usuario creado exitosamente con rol de estudiante")
            createdUserId = response.body.user.id
        })
    })

    it('Debe mostrar el usuario creado (GET /{user_id})', () => {
        cy.request('GET', `${API_URL}/user/${createdUserId}`).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.property('name')
            expect(response.body).to.have.property('id', createdUserId)
        })
    })

    it('Debe modificar el usuario creado (PUT)', () => {
          const modifyUser = {  
          "name": "Usuario de prueba actualizado",
          "email": "usuario_actualizado@example.com",
          "password": "contrasenasegura",       
        }

        cy.request('PUT', `${API_URL}/user/${createdUserId}`, modifyUser).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body).to.have.property('user')
            expect(response.body.message).to.eq("Usuario actualizado exitosamente")
        })
    })

    it('Debe eliminar el usuario creado (DELETE)', () => {
        cy.request('DELETE', API_URL+`/user/${createdUserId}`).then((response) => {
            expect(response.status).to.eq(200)
            expect(response.body.message).to.eq("Usuario eliminado exitosamente")
        })
    })
})
describe('Test de funcionalidad para el microservicio "seguridad"', () => {
    const API_URL = 'http://localhost:5001/api/reportes'

    it('Crear reporte de estudiante', () => {
      cy.request('GET', `${API_URL}/estudiante/20?format=pdf`).then((response) => {
          expect(response.status).to.eq(200)
      })
    })

    it('Crear reporte de curso', () => {
        cy.request('GET', `${API_URL}/curso/1?format=excel`).then((response) => {
            expect(response.status).to.eq(200)
        })
    })

    it('Crear reporte de tarea', () => {
        cy.request('GET', `${API_URL}/tarea/1?format=pdf`).then((response) => {
            expect(response.status).to.eq(200)
        })  
    })
})
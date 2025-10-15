describe('template spec', () => {
  beforeEach(() => {
    cy.visit('http://localhost:5173')
  })

  it('debe iniciar sesión correctamente', () => {
    cy.get('a[id="login-btn"]').click()
    cy.get('input[id="email"]').type('david@example.com')
    cy.get('input[id="password"]').type('password')
    cy.get('button[type="submit"]').click()
  })
})
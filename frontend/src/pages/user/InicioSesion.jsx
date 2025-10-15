import React, { useState } from 'react';
import { login } from '../../scripts/ApiSeguridad'; // Ajusta la ruta según tu estructura
import { useNavigate, redirect } from 'react-router-dom'

function InicioSesion() {
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null) // Limpia errores previos
    try {
      const data = await login({ email, password })
      console.log('Login exitoso:', data)
      // Aquí guardarías el token en el estado global o en localStorage
      // y redirigirías al usuario.
      // localStorage.setItem('authToken', data.token)
      return navigate('/')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h3 className="card-title text-center mb-4 section-title">Iniciar Sesión</h3>
              <form onSubmit={handleSubmit}>
                {/* Mensaje de error */}
                {error && <div className="alert alert-danger">{error}</div>}

                <div className="mb-3">
                  <label htmlFor="email" className="form-label fw-bold">Correo Electrónico</label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="password" className="form-label fw-bold">Contraseña</label>
                  <input
                    type="password"
                    className="form-control"
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>

                <div className="d-grid">
                  <button type="submit" className="site-button">Iniciar Sesión</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default InicioSesion
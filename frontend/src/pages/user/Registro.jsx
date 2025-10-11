import React, { useState } from 'react';
import { createUser } from '../../scripts/ApiSeguridad'; // Ajusta la ruta si es necesario

function Registro() {
  // Estado para cada campo del formulario
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirmation, setPasswordConfirmation] = useState('');

  // Estado para manejar mensajes de error y éxito
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccessMessage(null);

    // Validación simple: las contraseñas deben coincidir
    if (password !== passwordConfirmation) {
      setError('Las contraseñas no coinciden.');
      return;
    }

    const userData = {
      name,
      email,
      password,
      password_confirmation: passwordConfirmation,
    };

    try {
      const data = await createUser(userData);
      setSuccessMessage(`Usuario "${data.name}" creado con éxito. Ahora puedes iniciar sesión.`);
      // Opcional: Limpiar el formulario tras el éxito
      setName('');
      setEmail('');
      setPassword('');
      setPasswordConfirmation('');
    } catch (err) {
      // El error 'err' viene del 'throw' en ApiSeguridad.js
      setError(err.message);
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h3 className="card-title text-center mb-4 section-title">Crear una cuenta</h3>
              <form onSubmit={handleSubmit}>
                

                <div className="mb-3">
                  <label htmlFor="name" className="form-label fw-bold">Nombre Completo</label>
                  <input
                    type="text"
                    className="form-control"
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                </div>

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

                <div className="mb-3">
                  <label htmlFor="password_confirmation" className="form-label fw-bold">Confirmar Contraseña</label>
                  <input
                    type="password"
                    className="form-control"
                    id="password_confirmation"
                    value={passwordConfirmation}
                    onChange={(e) => setPasswordConfirmation(e.target.value)}
                    required
                  />
                </div>

                <div className="d-grid">
                    <button type="submit" className="site-button">Crear usuario</button>
                </div>

                {/* Mensajes de error o éxito */}
                {error && <div className="alert alert-danger mt-3">{error}</div>}
                {successMessage && <div className="alert alert-success mt-3">{successMessage}</div>}
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Registro;
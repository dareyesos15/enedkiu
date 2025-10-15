import React from 'react'
import { NavLink } from 'react-router-dom'
import '../App.css'

const Plantilla = ({ children }) => {
  return (
    <div className="app-container">
      {/* Header */}
      <header className="app-header">
        <div className="container-fluid">
          <div className="row align-items-center justify-content-between py-2">
            <div className="col-md-auto">
              <div className="logo-section">
                <NavLink to="/" className="text-decoration-none">
                  <h1 className="name-site mb-0">Enedkiu</h1>
                </NavLink>
              </div>
            </div>
            <div className="col-md-auto">
              <div className="user-section">
                <div className="btn-group">
                  <NavLink to="/inicio-sesion" className="btn btn-outline-light btn-header" id="login-btn">
                    Iniciar Sesión
                  </NavLink>
                  <NavLink to="/registro" className="btn btn-outline-light btn-header">
                    Registrarse
                  </NavLink>
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="main-navbar bg-white border-bottom">
        <div className="container-fluid">
          <ul className="nav nav-pills">
            <li className="nav-item">
              <NavLink
                to="/"
                className={({ isActive }) =>
                  `nav-link ${isActive ? 'active bg-success' : 'text-dark'}`
                }
              >
                Inicio
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/cursos"
                className={({ isActive }) =>
                  `nav-link ${isActive ? 'active bg-success' : 'text-dark'}`
                }
              >
                Cursos
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/mensajes"
                className={({ isActive }) =>
                  `nav-link ${isActive ? 'active bg-success' : 'text-dark'}`
                }
              >
                Mensajes
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/reportes"
                className={({ isActive }) =>
                  `nav-link ${isActive ? 'active bg-success' : 'text-dark'}`
                }
              >
                Reportes
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/gestion-usuarios"
                className={({ isActive }) =>
                  `nav-link ${isActive ? 'active bg-success' : 'text-dark'}`
                }
              >
                Usuarios
              </NavLink>
            </li>
          </ul>
        </div>
      </nav>

      {/* Main Content */}
      <main className="main-content bg-light">
        <div className="container-fluid py-4">
          <div className="row">

            {/* Content Area */}
            <section className="col">
              <div className="content-wrapper bg-white rounded shadow-sm p-4">
                {children} {/* Aquí va la funcionalidad de cada componente/vista*/}
              </div>
            </section>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="app-footer py-3">
        <div className="container-fluid">
          <div className="row">
            <div className="col-md-6">
              <p className="mb-0">&copy; 2025 Enedkiu. Todos los derechos reservados.</p>
            </div>
            <div className="col-md-6 text-end">
              <ul className="list-inline mb-0">
                <li className="list-inline-item">
                  <NavLink to="#" className="text-decoration-none">
                    Términos de uso
                  </NavLink>
                </li>
                <li className="list-inline-item">
                  <NavLink to="#" className="text-decoration-none">
                    Privacidad
                  </NavLink>
                </li>
                <li className="list-inline-item">
                  <NavLink to="#" className="text-decoration-none">
                    Soporte
                  </NavLink>
                </li>
                <li className="list-inline-item">
                  <NavLink to="#" className="text-decoration-none">
                    Contacto
                  </NavLink>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </footer>
    </div>
  )
}

export default Plantilla
import React from 'react'
import { NavLink } from 'react-router-dom'
import '../App.css'

const Plantilla = ({ children }) => {
  return (
    <div className="app-container">
      {/* Header */}
      <header className="app-header bg-primary text-white">
        <div className="container-fluid">
          <div className="row align-items-center py-2">
            <div className="col-md-4">
              <div className="logo-section">
                <NavLink to="/" className="text-decoration-none">
                  <h1 className="logo-text text-white mb-0">Enedkiu</h1>
                </NavLink>
              </div>
            </div>
            <div className="col-md-4">
              <div className="search-section">
                <div className="input-group">
                  <input 
                    type="text" 
                    className="form-control" 
                    placeholder="Buscar cursos, materiales..."
                  />
                  <button className="btn btn-success search-btn">
                    Buscar
                  </button>
                </div>
              </div>
            </div>
            <div className="col-md-4">
              <div className="user-section text-end">
                <div className="btn-group">
                  <NavLink to="/user/iniciosesion" className="btn btn-outline-light">
                    Iniciar Sesión
                  </NavLink>
                  <NavLink to="/user/registro" className="btn btn-success">
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
                to="/usuarios" 
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
            {/* Sidebar */}
            <aside className="col-md-3">
              <div className="sidebar bg-white rounded shadow-sm p-3">
                <h5 className="text-primary border-bottom pb-2">Accesos Rápidos</h5>
                <ul className="nav flex-column">
                  <li className="nav-item">
                    <NavLink 
                      to="/cursos" 
                      className={({ isActive }) => 
                        `nav-link text-dark ${isActive ? 'text-success fw-bold' : ''}`
                      }
                    >
                      Mis Cursos
                    </NavLink>
                  </li>
                  <li className="nav-item">
                    <NavLink 
                      to="/mensajes" 
                      className={({ isActive }) => 
                        `nav-link text-dark ${isActive ? 'text-success fw-bold' : ''}`
                      }
                    >
                      Bandeja de Entrada
                    </NavLink>
                  </li>
                  <li className="nav-item">
                    <NavLink 
                      to="/reportes" 
                      className={({ isActive }) => 
                        `nav-link text-dark ${isActive ? 'text-success fw-bold' : ''}`
                      }
                    >
                      Reportes Académicos
                    </NavLink>
                  </li>
                  <li className="nav-item">
                    <NavLink 
                      to="/user/perfil" 
                      className={({ isActive }) => 
                        `nav-link text-dark ${isActive ? 'text-success fw-bold' : ''}`
                      }
                    >
                      Mi Perfil
                    </NavLink>
                  </li>
                </ul>
              </div>
            </aside>

            {/* Content Area */}
            <section className="col-md-9">
              <div className="content-wrapper bg-white rounded shadow-sm p-4">
                {children}
              </div>
            </section>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="app-footer bg-primary text-white py-3">
        <div className="container-fluid">
          <div className="row">
            <div className="col-md-6">
              <p className="mb-0">&copy; 2025 Enedkiu. Todos los derechos reservados.</p>
            </div>
            <div className="col-md-6 text-end">
              <ul className="list-inline mb-0">
                <li className="list-inline-item">
                  <NavLink to="/terminos" className="text-white text-decoration-none">
                    Términos de uso
                  </NavLink>
                </li>
                <li className="list-inline-item">
                  <NavLink to="/privacidad" className="text-white text-decoration-none">
                    Privacidad
                  </NavLink>
                </li>
                <li className="list-inline-item">
                  <NavLink to="/soporte" className="text-white text-decoration-none">
                    Soporte
                  </NavLink>
                </li>
                <li className="list-inline-item">
                  <NavLink to="/contacto" className="text-white text-decoration-none">
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
import React from 'react'
import { Routes, Route } from 'react-router-dom'

import './App.css'

// Asumiendo que Plantilla también es una exportación por defecto
import Plantilla from './components/Plantilla'

// Importaciones de páginas principales
import Inicio from './pages/Inicio'
import Cursos from './pages/Cursos'
import Mensajes from './pages/Mensajes'
import Reportes from './pages/Reportes'

// Importaciones de páginas de usuario
import InicioSesion from './pages/user/InicioSesion'
import Perfil from './pages/user/Perfil'
import Registro from './pages/user/Registro'
import GestionUsuarios from './pages/user/GestionUsuarios'

function App() {
  const componentes = [
    [Inicio, "/"],
    [Cursos, "/cursos"],
    [Mensajes, "/mensajes"],
    [Reportes, "/reportes"],
    [InicioSesion, "/inicio-sesion"],
    [Perfil, "/perfil"],
    [Registro, "/registro"],
    [GestionUsuarios, "/gestion-usuarios"],
  ]

  return (
    <>
      <Routes>
        {componentes.map(([Componente, path]) => (
          <Route key={path} path={path} element={<Componente />} />
        ))}
      </Routes>
      
      <Plantilla componentes={componentes} />
    </>
    
  )
}

export default App
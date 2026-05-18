import React, { useState } from 'react'
import { Sidebar }        from './components/Sidebar.jsx'
import { ToastContainer } from './components/ToastContainer.jsx'
import { useToast }       from './hooks/useToast.js'
import { Dashboard }      from './pages/Dashboard.jsx'
import { Cajeras }        from './pages/Cajeras.jsx'
import { Clientes }       from './pages/Clientes.jsx'
import { Productos }      from './pages/Productos.jsx'
import { NuevaCompra }    from './pages/NuevaCompra.jsx'
import { Historial }      from './pages/Historial.jsx'

/**
 * Componente raíz de la aplicación.
 * Gestiona la navegación entre páginas y el sistema de notificaciones.
 */
export default function App() {
  const [page, setPage] = useState('dashboard')
  const { toasts, toast } = useToast()

  const renderPage = () => {
    switch (page) {
      case 'dashboard': return <Dashboard  onNavigate={setPage} />
      case 'cajeras':   return <Cajeras    toast={toast} />
      case 'clientes':  return <Clientes   toast={toast} />
      case 'productos': return <Productos  toast={toast} />
      case 'compra':    return <NuevaCompra toast={toast} />
      case 'historial': return <Historial  toast={toast} />
      default:          return <Dashboard  onNavigate={setPage} />
    }
  }

  return (
    <div className="layout">
      <Sidebar page={page} onNavigate={setPage} />
      <main className="main-content">
        {renderPage()}
      </main>
      <ToastContainer toasts={toasts} />
    </div>
  )
}

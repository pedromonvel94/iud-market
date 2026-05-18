import React from 'react'
import {
  LayoutDashboard, Users, ShoppingCart,
  Package, CreditCard, Activity
} from 'lucide-react'

const NAV = [
  { label: 'Dashboard',  icon: LayoutDashboard, page: 'dashboard' },
  { section: 'Gestión' },
  { label: 'Cajeras',    icon: CreditCard,       page: 'cajeras'   },
  { label: 'Clientes',   icon: Users,            page: 'clientes'  },
  { label: 'Productos',  icon: Package,          page: 'productos' },
  { section: 'Operaciones' },
  { label: 'Nueva Compra', icon: ShoppingCart,   page: 'compra'    },
  { label: 'Historial',    icon: Activity,       page: 'historial' },
]

/** Sidebar de navegación lateral */
export function Sidebar({ page, onNavigate }) {
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <h1>IUDMarket</h1>
        <span>Sistema de Cobro v1.0</span>
      </div>

      <nav className="sidebar-nav">
        {NAV.map((item, i) => {
          if (item.section) {
            return <p key={i} className="nav-section-label">{item.section}</p>
          }
          const Icon = item.icon
          return (
            <button
              key={item.page}
              className={`nav-item ${page === item.page ? 'active' : ''}`}
              onClick={() => onNavigate(item.page)}
            >
              <Icon className="nav-icon" />
              {item.label}
            </button>
          )
        })}
      </nav>

      <div className="sidebar-status">
        <p><span className="status-dot" />API conectada</p>
        <p style={{ marginTop: 4, fontSize: 10 }}>localhost:8080</p>
      </div>
    </aside>
  )
}

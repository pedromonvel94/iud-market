import React, { useEffect, useState } from 'react'
import { cajerasApi, clientesApi, productosApi, comprasApi } from '../services/api'
import { CreditCard, Users, Package, ShoppingCart, Clock, TrendingUp } from 'lucide-react'

/** Página de inicio con métricas generales del sistema */
export function Dashboard({ onNavigate }) {
  const [stats, setStats] = useState({
    cajeras: 0, cajerasDisp: 0,
    clientes: 0, productos: 0,
    compras: 0, totalVendido: 0,
  })
  const [comprasRecientes, setComprasRecientes] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function cargar() {
      try {
        const [caj, cajDisp, cli, prod, comp] = await Promise.all([
          cajerasApi.listar(),
          cajerasApi.disponibles(),
          clientesApi.listar(),
          productosApi.listar(),
          comprasApi.listar(),
        ])
        const compList = comp.data || []
        const total = compList.reduce((s, c) => s + (c.totalCompra || 0), 0)
        setStats({
          cajeras:     caj.data?.length  || 0,
          cajerasDisp: cajDisp.data?.length || 0,
          clientes:    cli.data?.length  || 0,
          productos:   prod.data?.length || 0,
          compras:     compList.length,
          totalVendido: total,
        })
        setComprasRecientes(compList.slice(-5).reverse())
      } catch (e) {
        console.error('Error cargando dashboard:', e)
      } finally {
        setLoading(false)
      }
    }
    cargar()
  }, [])

  return (
    <div className="fade-in">
      <div className="page-header">
        <h2>Dashboard</h2>
        <p>Resumen del sistema de cobro en tiempo real</p>
      </div>

      {/* Stats */}
      <div className="grid-4" style={{ marginBottom: 24 }}>
        <div className="stat-card green">
          <div className="stat-label">Cajeras disponibles</div>
          <div className="stat-value">{loading ? '—' : stats.cajerasDisp}</div>
          <div style={{ fontSize: 11, color: 'var(--text-muted)', marginTop: 6, fontFamily: 'var(--font-mono)' }}>
            de {stats.cajeras} totales
          </div>
        </div>
        <div className="stat-card blue">
          <div className="stat-label">Clientes registrados</div>
          <div className="stat-value">{loading ? '—' : stats.clientes}</div>
        </div>
        <div className="stat-card amber">
          <div className="stat-label">Productos en sistema</div>
          <div className="stat-value">{loading ? '—' : stats.productos}</div>
        </div>
        <div className="stat-card green">
          <div className="stat-label">Total compras</div>
          <div className="stat-value">{loading ? '—' : stats.compras}</div>
        </div>
      </div>

      {/* Total vendido */}
      <div className="card" style={{ marginBottom: 24, borderColor: 'rgba(0,255,136,0.2)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <TrendingUp size={24} color="var(--green)" />
          <div>
            <div style={{ fontFamily: 'var(--font-mono)', fontSize: 10, letterSpacing: 2, color: 'var(--text-muted)', textTransform: 'uppercase' }}>
              Total acumulado vendido
            </div>
            <div style={{ fontFamily: 'var(--font-display)', fontSize: 36, fontWeight: 800, color: 'var(--green)' }}>
              ${loading ? '—' : stats.totalVendido.toLocaleString('es-CO', { minimumFractionDigits: 2 })}
            </div>
          </div>
        </div>
      </div>

      {/* Compras recientes */}
      <div className="card">
        <div className="card-header">
          <span className="card-title">Compras recientes</span>
          <button className="btn btn-ghost btn-sm" onClick={() => onNavigate('historial')}>
            Ver todas
          </button>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>#</th>
                <th>Cliente</th>
                <th>Cajera</th>
                <th>Total</th>
                <th>Tiempo (s)</th>
                <th>Fecha</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr className="loading-row">
                  <td colSpan={6}><div className="spinner" style={{ margin: '0 auto' }} /></td>
                </tr>
              )}
              {!loading && comprasRecientes.length === 0 && (
                <tr><td colSpan={6} style={{ textAlign: 'center', padding: 32, color: 'var(--text-muted)' }}>
                  Sin compras aún — <button className="btn btn-ghost btn-sm" onClick={() => onNavigate('compra')}>procesar primera compra</button>
                </td></tr>
              )}
              {comprasRecientes.map(c => (
                <tr key={c.id}>
                  <td>#{c.id}</td>
                  <td className="td-name">{c.cliente?.nombre || '—'}</td>
                  <td>{c.cajera?.nombre || '—'}</td>
                  <td style={{ color: 'var(--green)' }}>${(c.totalCompra || 0).toFixed(2)}</td>
                  <td>
                    <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                      <Clock size={11} color="var(--text-muted)" />
                      {c.tiempoTotalProcesamiento}s
                    </span>
                  </td>
                  <td style={{ color: 'var(--text-muted)' }}>
                    {c.fechaCompra ? new Date(c.fechaCompra).toLocaleString('es-CO') : '—'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

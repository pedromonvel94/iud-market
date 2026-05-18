import React, { useEffect, useState } from 'react'
import { comprasApi } from '../services/api'
import { RefreshCw, Eye, Clock, X } from 'lucide-react'

/** Historial de todas las compras con detalle por compra */
export function Historial({ toast }) {
  const [compras,  setCompras]  = useState([])
  const [loading,  setLoading]  = useState(true)
  const [detalle,  setDetalle]  = useState(null)   // compra seleccionada
  const [items,    setItems]    = useState([])
  const [loadDet,  setLoadDet]  = useState(false)

  const cargar = async () => {
    setLoading(true)
    try {
      const res = await comprasApi.listar()
      setCompras((res.data || []).reverse())
    } catch (e) {
      toast.error('Error al cargar historial: ' + e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { cargar() }, [])

  const verDetalle = async (compra) => {
    setDetalle(compra)
    setItems([])
    setLoadDet(true)
    try {
      const res = await comprasApi.detalle(compra.id)
      setItems(res.data || [])
    } catch (e) {
      toast.error('Error al cargar detalle: ' + e.message)
    } finally {
      setLoadDet(false)
    }
  }

  const totalTiempo = compras.reduce((s, c) => s + (c.tiempoTotalProcesamiento || 0), 0)
  const totalVendido = compras.reduce((s, c) => s + (c.totalCompra || 0), 0)

  return (
    <div className="fade-in">
      <div className="page-header">
        <h2>Historial de Compras</h2>
        <p>Registro completo de todas las transacciones procesadas</p>
      </div>

      {/* Resumen */}
      <div className="grid-3" style={{ marginBottom: 20 }}>
        <div className="stat-card green">
          <div className="stat-label">Total compras</div>
          <div className="stat-value">{compras.length}</div>
        </div>
        <div className="stat-card amber">
          <div className="stat-label">Total vendido</div>
          <div className="stat-value" style={{ fontSize: 22 }}>
            ${totalVendido.toLocaleString('es-CO', { minimumFractionDigits: 2 })}
          </div>
        </div>
        <div className="stat-card blue">
          <div className="stat-label">Tiempo total procesado</div>
          <div className="stat-value">{totalTiempo}s</div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <span className="card-title">Todas las compras</span>
          <button className="btn btn-ghost btn-sm" onClick={cargar}>
            <RefreshCw size={13} /> Actualizar
          </button>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>#</th><th>Cliente</th><th>Cajera</th>
                <th>Caja</th><th>Total</th><th>Tiempo</th>
                <th>Fecha</th><th>Detalle</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr className="loading-row">
                  <td colSpan={8}><div className="spinner" style={{ margin: '0 auto' }} /></td>
                </tr>
              )}
              {!loading && compras.length === 0 && (
                <tr><td colSpan={8} style={{ textAlign: 'center', padding: 32, color: 'var(--text-muted)' }}>
                  Sin compras registradas
                </td></tr>
              )}
              {compras.map(c => (
                <tr key={c.id}>
                  <td style={{ color: 'var(--text-muted)' }}>#{c.id}</td>
                  <td className="td-name">{c.cliente?.nombre || '—'}</td>
                  <td>{c.cajera?.nombre || '—'}</td>
                  <td style={{ color: 'var(--text-muted)' }}>#{c.cajera?.numeroCaja}</td>
                  <td style={{ color: 'var(--green)', fontFamily: 'var(--font-mono)' }}>
                    ${(c.totalCompra || 0).toFixed(2)}
                  </td>
                  <td>
                    <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                      <Clock size={11} color="var(--amber)" />
                      <span style={{ color: 'var(--amber)', fontFamily: 'var(--font-mono)' }}>
                        {c.tiempoTotalProcesamiento}s
                      </span>
                    </span>
                  </td>
                  <td style={{ color: 'var(--text-muted)', fontSize: 11 }}>
                    {c.fechaCompra ? new Date(c.fechaCompra).toLocaleString('es-CO') : '—'}
                  </td>
                  <td>
                    <button className="btn btn-ghost btn-sm" onClick={() => verDetalle(c)}>
                      <Eye size={12} /> Ver
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal detalle */}
      {detalle && (
        <div className="modal-overlay" onClick={() => setDetalle(null)}>
          <div className="modal" style={{ maxWidth: 560 }} onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-title">Compra #{detalle.id}</span>
              <button className="btn btn-ghost btn-sm" onClick={() => setDetalle(null)}>
                <X size={14} />
              </button>
            </div>

            {/* Info compra */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 20 }}>
              <div>
                <div className="form-label">Cliente</div>
                <div style={{ fontWeight: 600 }}>{detalle.cliente?.nombre}</div>
              </div>
              <div>
                <div className="form-label">Cajera</div>
                <div style={{ fontWeight: 600 }}>{detalle.cajera?.nombre} — Caja #{detalle.cajera?.numeroCaja}</div>
              </div>
              <div>
                <div className="form-label">Total cobrado</div>
                <div style={{ fontFamily: 'var(--font-display)', fontSize: 22, fontWeight: 800, color: 'var(--green)' }}>
                  ${(detalle.totalCompra || 0).toFixed(2)}
                </div>
              </div>
              <div>
                <div className="form-label">Tiempo total</div>
                <div style={{ fontFamily: 'var(--font-display)', fontSize: 22, fontWeight: 800, color: 'var(--amber)' }}>
                  {detalle.tiempoTotalProcesamiento}s
                </div>
              </div>
            </div>

            {/* Productos */}
            <div className="form-label" style={{ marginBottom: 8 }}>Productos comprados</div>
            {loadDet && <div className="spinner" style={{ margin: '20px auto' }} />}
            {!loadDet && items.length === 0 && (
              <p style={{ color: 'var(--text-muted)', fontSize: 13, textAlign: 'center', padding: 16 }}>
                Sin detalle disponible
              </p>
            )}
            {items.map((item, i) => (
              <div key={i} style={{
                display: 'grid', gridTemplateColumns: '1fr auto auto auto',
                gap: 12, padding: '10px 12px', marginBottom: 6,
                background: 'var(--bg)', border: '1px solid var(--border)',
                borderRadius: 'var(--radius)', alignItems: 'center'
              }}>
                <div>
                  <div style={{ fontWeight: 500, fontSize: 13 }}>{item.nombreProducto}</div>
                  <div style={{ fontSize: 11, color: 'var(--text-muted)', fontFamily: 'var(--font-mono)' }}>
                    {item.cantidad} × ${item.precioUnitario?.toFixed(2)}
                  </div>
                </div>
                <span style={{ color: 'var(--green)', fontFamily: 'var(--font-mono)', fontSize: 13 }}>
                  ${item.subtotal?.toFixed(2)}
                </span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4, fontSize: 11 }}>
                  <Clock size={10} color="var(--amber)" />
                  <span style={{ color: 'var(--amber)', fontFamily: 'var(--font-mono)' }}>
                    {item.tiempoProcesamiento}s
                  </span>
                </span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

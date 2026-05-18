import React, { useEffect, useState } from 'react'
import { productosApi } from '../services/api'
import { Plus, Trash2, RefreshCw, Clock } from 'lucide-react'

/** Gestión de productos: nombre, precio y tiempo de procesamiento */
export function Productos({ toast }) {
  const [productos, setProductos] = useState([])
  const [loading,   setLoading]   = useState(true)
  const [modal,     setModal]     = useState(false)
  const [saving,    setSaving]    = useState(false)
  const [form, setForm] = useState({ nombre: '', precio: '', tiempoProcesamiento: '' })

  const cargar = async () => {
    setLoading(true)
    try {
      const res = await productosApi.listar()
      setProductos(res.data || [])
    } catch (e) {
      toast.error('Error al cargar productos: ' + e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { cargar() }, [])

  const handleCrear = async (e) => {
    e.preventDefault()
    if (!form.nombre.trim() || !form.precio || !form.tiempoProcesamiento)
      return toast.error('Completa todos los campos')
    setSaving(true)
    try {
      await productosApi.crear({
        nombre: form.nombre,
        precio: parseFloat(form.precio),
        tiempoProcesamiento: parseInt(form.tiempoProcesamiento),
      })
      toast.success('Producto creado correctamente')
      setModal(false)
      setForm({ nombre: '', precio: '', tiempoProcesamiento: '' })
      cargar()
    } catch (e) {
      toast.error('Error al crear producto: ' + e.message)
    } finally {
      setSaving(false)
    }
  }

  const handleEliminar = async (id) => {
    if (!confirm('¿Eliminar este producto?')) return
    try {
      await productosApi.eliminar(id)
      toast.success('Producto eliminado')
      cargar()
    } catch (e) {
      toast.error('Error: ' + e.message)
    }
  }

  return (
    <div className="fade-in">
      <div className="page-header">
        <h2>Productos</h2>
        <p>Catálogo de productos con precio y tiempo de procesamiento en caja</p>
      </div>

      <div className="card">
        <div className="card-header">
          <span className="card-title">Productos ({productos.length})</span>
          <div style={{ display: 'flex', gap: 8 }}>
            <button className="btn btn-ghost btn-sm" onClick={cargar}><RefreshCw size={13} /> Actualizar</button>
            <button className="btn btn-primary btn-sm" onClick={() => setModal(true)}><Plus size={13} /> Nuevo producto</button>
          </div>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr><th>ID</th><th>Nombre</th><th>Precio</th><th>Tiempo proc.</th><th>Acciones</th></tr>
            </thead>
            <tbody>
              {loading && (
                <tr className="loading-row"><td colSpan={5}><div className="spinner" style={{ margin: '0 auto' }} /></td></tr>
              )}
              {!loading && productos.length === 0 && (
                <tr><td colSpan={5} style={{ textAlign: 'center', padding: 32, color: 'var(--text-muted)' }}>Sin productos registrados</td></tr>
              )}
              {productos.map(p => (
                <tr key={p.id}>
                  <td style={{ color: 'var(--text-muted)' }}>#{p.id}</td>
                  <td className="td-name">{p.nombre}</td>
                  <td style={{ color: 'var(--green)', fontFamily: 'var(--font-mono)' }}>
                    ${(p.precio || 0).toFixed(2)}
                  </td>
                  <td>
                    <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
                      <Clock size={11} color="var(--amber)" />
                      <span style={{ color: 'var(--amber)', fontFamily: 'var(--font-mono)' }}>
                        {p.tiempoProcesamiento}s
                      </span>
                    </span>
                  </td>
                  <td>
                    <button className="btn btn-danger btn-sm" onClick={() => handleEliminar(p.id)}>
                      <Trash2 size={12} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {modal && (
        <div className="modal-overlay" onClick={() => setModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-title">Nuevo Producto</span>
              <button className="btn btn-ghost btn-sm" onClick={() => setModal(false)}>✕</button>
            </div>
            <form onSubmit={handleCrear}>
              <div className="form-group">
                <label className="form-label">Nombre del producto</label>
                <input className="form-input" placeholder="Ej: Leche entera 1L"
                  value={form.nombre} onChange={e => setForm(f => ({ ...f, nombre: e.target.value }))} />
              </div>
              <div className="form-group">
                <label className="form-label">Precio unitario ($)</label>
                <input className="form-input" type="number" step="0.01" min="0" placeholder="Ej: 3200.00"
                  value={form.precio} onChange={e => setForm(f => ({ ...f, precio: e.target.value }))} />
              </div>
              <div className="form-group">
                <label className="form-label">Tiempo de procesamiento (segundos)</label>
                <input className="form-input" type="number" min="1" placeholder="Ej: 2"
                  value={form.tiempoProcesamiento}
                  onChange={e => setForm(f => ({ ...f, tiempoProcesamiento: e.target.value }))} />
                <span style={{ fontSize: 11, color: 'var(--text-muted)', fontFamily: 'var(--font-mono)', marginTop: 4, display: 'block' }}>
                  Simula cuánto tarda la cajera en escanear este producto
                </span>
              </div>
              <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 20 }}>
                <button type="button" className="btn btn-ghost" onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  {saving ? <><div className="spinner" style={{ width: 14, height: 14 }} />Guardando…</> : 'Crear producto'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

import React, { useEffect, useState } from 'react'
import { cajerasApi } from '../services/api'
import { Plus, Trash2, RefreshCw } from 'lucide-react'

/** Gestión de cajeras — alta, consulta, cambio de estado y eliminación */
export function Cajeras({ toast }) {
  const [cajeras,  setCajeras]  = useState([])
  const [loading,  setLoading]  = useState(true)
  const [modal,    setModal]    = useState(false)
  const [saving,   setSaving]   = useState(false)
  const [form, setForm] = useState({ nombre: '', numeroCaja: '', estado: 'DISPONIBLE' })

  const cargar = async () => {
    setLoading(true)
    try {
      const res = await cajerasApi.listar()
      setCajeras(res.data || [])
    } catch (e) {
      toast.error('Error al cargar cajeras: ' + e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { cargar() }, [])

  const handleCrear = async (e) => {
    e.preventDefault()
    if (!form.nombre.trim() || !form.numeroCaja) return toast.error('Completa todos los campos')
    setSaving(true)
    try {
      await cajerasApi.crear({ ...form, numeroCaja: Number(form.numeroCaja) })
      toast.success('Cajera creada correctamente')
      setModal(false)
      setForm({ nombre: '', numeroCaja: '', estado: 'DISPONIBLE' })
      cargar()
    } catch (e) {
      toast.error('Error al crear cajera: ' + e.message)
    } finally {
      setSaving(false)
    }
  }

  const handleEstado = async (cajera) => {
    const nuevo = cajera.estado === 'DISPONIBLE' ? 'FUERA_DE_SERVICIO' : 'DISPONIBLE'
    try {
      await cajerasApi.actualizarEstado(cajera.id, nuevo)
      toast.success(`Cajera ${nuevo === 'DISPONIBLE' ? 'habilitada' : 'deshabilitada'}`)
      cargar()
    } catch (e) {
      toast.error('Error: ' + e.message)
    }
  }

  const handleEliminar = async (id) => {
    if (!confirm('¿Eliminar esta cajera?')) return
    try {
      await cajerasApi.eliminar(id)
      toast.success('Cajera eliminada')
      cargar()
    } catch (e) {
      toast.error('Error al eliminar: ' + e.message)
    }
  }

  const estadoBadge = (e) => {
    if (e === 'DISPONIBLE')        return <span className="badge badge-green">DISPONIBLE</span>
    if (e === 'OCUPADA')           return <span className="badge badge-amber">OCUPADA</span>
    return                                <span className="badge badge-muted">{e}</span>
  }

  return (
    <div className="fade-in">
      <div className="page-header">
        <h2>Cajeras</h2>
        <p>Gestión del personal de caja y sus estados</p>
      </div>

      <div className="card">
        <div className="card-header">
          <span className="card-title">Cajeras registradas</span>
          <div style={{ display: 'flex', gap: 8 }}>
            <button className="btn btn-ghost btn-sm" onClick={cargar}>
              <RefreshCw size={13} /> Actualizar
            </button>
            <button className="btn btn-primary btn-sm" onClick={() => setModal(true)}>
              <Plus size={13} /> Nueva cajera
            </button>
          </div>
        </div>

        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th><th>Nombre</th><th>N° Caja</th>
                <th>Estado</th><th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr className="loading-row">
                  <td colSpan={5}><div className="spinner" style={{ margin: '0 auto' }} /></td>
                </tr>
              )}
              {!loading && cajeras.length === 0 && (
                <tr><td colSpan={5} style={{ textAlign: 'center', padding: 32, color: 'var(--text-muted)' }}>
                  No hay cajeras registradas
                </td></tr>
              )}
              {cajeras.map(c => (
                <tr key={c.id}>
                  <td style={{ color: 'var(--text-muted)' }}>#{c.id}</td>
                  <td className="td-name">{c.nombre}</td>
                  <td>Caja #{c.numeroCaja}</td>
                  <td>{estadoBadge(c.estado)}</td>
                  <td>
                    <div style={{ display: 'flex', gap: 6 }}>
                      <button
                        className={`btn btn-sm ${c.estado === 'DISPONIBLE' ? 'btn-ghost' : 'btn-primary'}`}
                        onClick={() => handleEstado(c)}
                        disabled={c.estado === 'OCUPADA'}
                      >
                        {c.estado === 'DISPONIBLE' ? 'Deshabilitar' : 'Habilitar'}
                      </button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleEliminar(c.id)}>
                        <Trash2 size={12} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal crear cajera */}
      {modal && (
        <div className="modal-overlay" onClick={() => setModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-title">Nueva Cajera</span>
              <button className="btn btn-ghost btn-sm" onClick={() => setModal(false)}>✕</button>
            </div>
            <form onSubmit={handleCrear}>
              <div className="form-group">
                <label className="form-label">Nombre completo</label>
                <input className="form-input" placeholder="Ej: María González"
                  value={form.nombre} onChange={e => setForm(f => ({ ...f, nombre: e.target.value }))} />
              </div>
              <div className="form-group">
                <label className="form-label">Número de caja</label>
                <input className="form-input" type="number" min="1" placeholder="Ej: 1"
                  value={form.numeroCaja} onChange={e => setForm(f => ({ ...f, numeroCaja: e.target.value }))} />
              </div>
              <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 20 }}>
                <button type="button" className="btn btn-ghost" onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  {saving ? <><div className="spinner" style={{ width: 14, height: 14 }} /> Guardando…</> : 'Crear cajera'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

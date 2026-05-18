import React, { useEffect, useState } from 'react'
import { clientesApi } from '../services/api'
import { Plus, Trash2, RefreshCw } from 'lucide-react'

/** Gestión de clientes del supermercado */
export function Clientes({ toast }) {
  const [clientes, setClientes] = useState([])
  const [loading,  setLoading]  = useState(true)
  const [modal,    setModal]    = useState(false)
  const [saving,   setSaving]   = useState(false)
  const [form, setForm] = useState({ nombre: '', documento: '' })

  const cargar = async () => {
    setLoading(true)
    try {
      const res = await clientesApi.listar()
      setClientes(res.data || [])
    } catch (e) {
      toast.error('Error al cargar clientes: ' + e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { cargar() }, [])

  const handleCrear = async (e) => {
    e.preventDefault()
    if (!form.nombre.trim() || !form.documento.trim()) return toast.error('Completa todos los campos')
    setSaving(true)
    try {
      await clientesApi.crear(form)
      toast.success('Cliente registrado correctamente')
      setModal(false)
      setForm({ nombre: '', documento: '' })
      cargar()
    } catch (e) {
      toast.error('Error al crear cliente: ' + e.message)
    } finally {
      setSaving(false)
    }
  }

  const handleEliminar = async (id) => {
    if (!confirm('¿Eliminar este cliente?')) return
    try {
      await clientesApi.eliminar(id)
      toast.success('Cliente eliminado')
      cargar()
    } catch (e) {
      toast.error('Error: ' + e.message)
    }
  }

  return (
    <div className="fade-in">
      <div className="page-header">
        <h2>Clientes</h2>
        <p>Registro de clientes del supermercado</p>
      </div>

      <div className="card">
        <div className="card-header">
          <span className="card-title">Clientes registrados ({clientes.length})</span>
          <div style={{ display: 'flex', gap: 8 }}>
            <button className="btn btn-ghost btn-sm" onClick={cargar}><RefreshCw size={13} /> Actualizar</button>
            <button className="btn btn-primary btn-sm" onClick={() => setModal(true)}><Plus size={13} /> Nuevo cliente</button>
          </div>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr><th>ID</th><th>Nombre</th><th>Documento</th><th>Acciones</th></tr>
            </thead>
            <tbody>
              {loading && (
                <tr className="loading-row"><td colSpan={4}><div className="spinner" style={{ margin: '0 auto' }} /></td></tr>
              )}
              {!loading && clientes.length === 0 && (
                <tr><td colSpan={4} style={{ textAlign: 'center', padding: 32, color: 'var(--text-muted)' }}>Sin clientes registrados</td></tr>
              )}
              {clientes.map(c => (
                <tr key={c.id}>
                  <td style={{ color: 'var(--text-muted)' }}>#{c.id}</td>
                  <td className="td-name">{c.nombre}</td>
                  <td>{c.documento}</td>
                  <td>
                    <button className="btn btn-danger btn-sm" onClick={() => handleEliminar(c.id)}>
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
              <span className="modal-title">Nuevo Cliente</span>
              <button className="btn btn-ghost btn-sm" onClick={() => setModal(false)}>✕</button>
            </div>
            <form onSubmit={handleCrear}>
              <div className="form-group">
                <label className="form-label">Nombre completo</label>
                <input className="form-input" placeholder="Ej: Juan Pérez"
                  value={form.nombre} onChange={e => setForm(f => ({ ...f, nombre: e.target.value }))} />
              </div>
              <div className="form-group">
                <label className="form-label">Documento de identidad</label>
                <input className="form-input" placeholder="Ej: 1012345678"
                  value={form.documento} onChange={e => setForm(f => ({ ...f, documento: e.target.value }))} />
              </div>
              <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 20 }}>
                <button type="button" className="btn btn-ghost" onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  {saving ? <><div className="spinner" style={{ width: 14, height: 14 }} />Guardando…</> : 'Registrar cliente'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

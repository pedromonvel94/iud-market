/**
 * Capa de servicios API
 * Centraliza todas las llamadas HTTP al backend Spring Boot
 * Base URL configurada en vite.config.js (proxy → localhost:8080)
 */
import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' },
})

// Interceptor de respuesta: manejo centralizado de errores
api.interceptors.response.use(
  res => res,
  err => {
    const msg = err.response?.data?.message
      || err.response?.data
      || err.message
      || 'Error de conexión con el servidor'
    return Promise.reject(new Error(String(msg)))
  }
)

/* ── CAJERAS ──────────────────────────────────────────────────── */
export const cajerasApi = {
  listar:       ()          => api.get('/cajeras'),
  disponibles:  ()          => api.get('/cajeras/disponibles'),
  crear:        (data)      => api.post('/cajeras', data),
  actualizarEstado: (id, e) => api.put(`/cajeras/${id}/estado?nuevoEstado=${e}`),
  eliminar:     (id)        => api.delete(`/cajeras/${id}`),
}

/* ── CLIENTES ─────────────────────────────────────────────────── */
export const clientesApi = {
  listar:  ()     => api.get('/clientes'),
  crear:   (data) => api.post('/clientes', data),
  eliminar:(id)   => api.delete(`/clientes/${id}`),
}

/* ── PRODUCTOS ────────────────────────────────────────────────── */
export const productosApi = {
  listar:  ()     => api.get('/productos'),
  crear:   (data) => api.post('/productos', data),
  eliminar:(id)   => api.delete(`/productos/${id}`),
}

/* ── COMPRAS ──────────────────────────────────────────────────── */
export const comprasApi = {
  listar:        ()        => api.get('/compras'),
  porCliente:    (id)      => api.get(`/compras/cliente/${id}`),
  detalle:       (id)      => api.get(`/compras/${id}/detalle`),
  procesar:      (data)    => api.post('/compras/procesar', data),
}

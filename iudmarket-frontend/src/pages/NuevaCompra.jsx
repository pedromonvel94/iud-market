import React, { useEffect, useState, useRef } from 'react'
import { clientesApi, cajerasApi, productosApi, comprasApi } from '../services/api'
import { ShoppingCart, Plus, Trash2, Zap, Clock, CheckCircle } from 'lucide-react'

/**
 * Página de procesamiento de compra.
 * Muestra en tiempo real el log del hilo que está procesando la compra,
 * simulando el proceso de cobro de la cajera producto por producto.
 */
export function NuevaCompra({ toast }) {
  const [clientes,  setClientes]  = useState([])
  const [cajeras,   setCajeras]   = useState([])
  const [productos, setProductos] = useState([])

  const [clienteId, setClienteId] = useState('')
  const [cajeraId,  setCajeraId]  = useState('')
  const [carrito,   setCarrito]   = useState([])   // [{productoId, cantidad}]
  const [prodSel,   setProdSel]   = useState('')
  const [cantSel,   setCantSel]   = useState(1)

  const [procesando, setProcesando] = useState(false)
  const [log,        setLog]        = useState([])
  const [resultado,  setResultado]  = useState(null)
  const logRef = useRef(null)

  // Cargar datos iniciales
  useEffect(() => {
    async function init() {
      try {
        const [cli, caj, prod] = await Promise.all([
          clientesApi.listar(),
          cajerasApi.disponibles(),
          productosApi.listar(),
        ])
        setClientes(cli.data  || [])
        setCajeras(caj.data   || [])
        setProductos(prod.data|| [])
      } catch (e) {
        toast.error('Error cargando datos: ' + e.message)
      }
    }
    init()
  }, [])

  // Auto-scroll del log
  useEffect(() => {
    if (logRef.current) logRef.current.scrollTop = logRef.current.scrollHeight
  }, [log])

  const addLog = (msg, tipo = 'muted') => {
    setLog(prev => [...prev, { msg, tipo, t: new Date().toLocaleTimeString('es-CO') }])
  }

  const agregarProducto = () => {
    if (!prodSel) return toast.error('Selecciona un producto')
    if (cantSel < 1) return toast.error('La cantidad debe ser mayor a 0')
    const ya = carrito.find(c => c.productoId === Number(prodSel))
    if (ya) {
      setCarrito(prev => prev.map(c =>
        c.productoId === Number(prodSel)
          ? { ...c, cantidad: c.cantidad + Number(cantSel) }
          : c
      ))
    } else {
      setCarrito(prev => [...prev, { productoId: Number(prodSel), cantidad: Number(cantSel) }])
    }
    setProdSel('')
    setCantSel(1)
  }

  const quitarProducto = (id) => setCarrito(prev => prev.filter(c => c.productoId !== id))

  const getProducto = (id) => productos.find(p => p.id === id)

  const totalEstimado = carrito.reduce((s, item) => {
    const p = getProducto(item.productoId)
    return s + (p ? p.precio * item.cantidad : 0)
  }, 0)

  const tiempoEstimado = carrito.reduce((s, item) => {
    const p = getProducto(item.productoId)
    return s + (p ? p.tiempoProcesamiento * item.cantidad : 0)
  }, 0)

  const procesarCompra = async () => {
    if (!clienteId) return toast.error('Selecciona un cliente')
    if (!cajeraId)  return toast.error('Selecciona una cajera disponible')
    if (carrito.length === 0) return toast.error('Agrega al menos un producto al carrito')

    setProcesando(true)
    setResultado(null)
    setLog([])

    const cliente = clientes.find(c => c.id === Number(clienteId))
    const cajera  = cajeras.find(c => c.id === Number(cajeraId))

    addLog(`Iniciando cobro para ${cliente?.nombre}`, 'green')
    addLog(`Cajera asignada: ${cajera?.nombre} — Caja #${cajera?.numeroCaja}`, 'amber')
    addLog('─'.repeat(40), 'muted')

    // Simulación visual del progreso producto por producto
    for (const item of carrito) {
      const p = getProducto(item.productoId)
      if (!p) continue
      addLog(`⟳ Escaneando: ${p.nombre} × ${item.cantidad}`, 'muted')
      // Espera visual (0.5s por unidad de tiempo, igual que el backend)
      await new Promise(r => setTimeout(r, p.tiempoProcesamiento * item.cantidad * 500))
      addLog(`✓ ${p.nombre} — $${(p.precio * item.cantidad).toFixed(2)} | ${p.tiempoProcesamiento * item.cantidad}s`, 'green')
    }

    addLog('─'.repeat(40), 'muted')
    addLog('Enviando al servidor...', 'amber')

    try {
      const res = await comprasApi.procesar({
        clienteId:  Number(clienteId),
        cajeraId:   Number(cajeraId),
        productos:  carrito.map(c => ({ idProducto: c.productoId, cantidad: c.cantidad })),
      })
      const compra = res.data
      addLog(`✅ Compra #${compra.id} procesada exitosamente`, 'green')
      addLog(`💰 Total: $${(compra.totalCompra || 0).toFixed(2)}`, 'green')
      addLog(`⏱  Tiempo total: ${compra.tiempoTotalProcesamiento}s`, 'amber')
      setResultado(compra)
      toast.success(`Compra #${compra.id} procesada — $${(compra.totalCompra || 0).toFixed(2)}`)
      // Limpiar carrito
      setCarrito([])
      // Recargar cajeras disponibles
      const cajAct = await cajerasApi.disponibles()
      setCajeras(cajAct.data || [])
      setCajeraId('')
    } catch (e) {
      addLog('✘ Error: ' + e.message, 'red')
      toast.error('Error al procesar compra: ' + e.message)
    } finally {
      setProcesando(false)
    }
  }

  return (
    <div className="fade-in">
      <div className="page-header">
        <h2>Nueva Compra</h2>
        <p>Simulación del proceso de cobro con concurrencia por hilos</p>
      </div>

      <div className="grid-2" style={{ gap: 20, alignItems: 'start' }}>

        {/* Columna izquierda: formulario */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

          {/* Selección cliente y cajera */}
          <div className="card">
            <div className="card-header" style={{ marginBottom: 16 }}>
              <span className="card-title">Datos de la compra</span>
            </div>
            <div className="form-group">
              <label className="form-label">Cliente</label>
              <select className="form-select" value={clienteId}
                onChange={e => setClienteId(e.target.value)} disabled={procesando}>
                <option value="">— Seleccionar cliente —</option>
                {clientes.map(c => (
                  <option key={c.id} value={c.id}>{c.nombre} · {c.documento}</option>
                ))}
              </select>
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Cajera disponible</label>
              <select className="form-select" value={cajeraId}
                onChange={e => setCajeraId(e.target.value)} disabled={procesando}>
                <option value="">— Seleccionar cajera —</option>
                {cajeras.map(c => (
                  <option key={c.id} value={c.id}>{c.nombre} — Caja #{c.numeroCaja}</option>
                ))}
              </select>
              {cajeras.length === 0 && (
                <p style={{ fontSize: 11, color: 'var(--red)', marginTop: 4, fontFamily: 'var(--font-mono)' }}>
                  Sin cajeras disponibles en este momento
                </p>
              )}
            </div>
          </div>

          {/* Agregar productos */}
          <div className="card">
            <div className="card-header" style={{ marginBottom: 16 }}>
              <span className="card-title">Carrito de productos</span>
            </div>

            {/* Selector de producto */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr auto auto', gap: 8, marginBottom: 16 }}>
              <select className="form-select" value={prodSel}
                onChange={e => setProdSel(e.target.value)} disabled={procesando}>
                <option value="">— Seleccionar producto —</option>
                {productos.map(p => (
                  <option key={p.id} value={p.id}>
                    {p.nombre} · ${p.precio} · {p.tiempoProcesamiento}s
                  </option>
                ))}
              </select>
              <input className="form-input" type="number" min="1" value={cantSel}
                onChange={e => setCantSel(Number(e.target.value))}
                style={{ width: 70 }} disabled={procesando} />
              <button className="btn btn-ghost" onClick={agregarProducto} disabled={procesando}>
                <Plus size={15} />
              </button>
            </div>

            {/* Lista del carrito */}
            {carrito.length === 0 && (
              <div className="empty-state" style={{ padding: 24 }}>
                <ShoppingCart size={32} style={{ margin: '0 auto 8px', display: 'block' }} />
                <p>Carrito vacío</p>
              </div>
            )}
            {carrito.map(item => {
              const p = getProducto(item.productoId)
              if (!p) return null
              return (
                <div key={item.productoId} className="producto-item">
                  <div>
                    <div className="producto-item-name">{p.nombre}</div>
                    <div style={{ fontSize: 11, color: 'var(--text-muted)', fontFamily: 'var(--font-mono)' }}>
                      {item.cantidad} × ${p.precio} · {p.tiempoProcesamiento * item.cantidad}s
                    </div>
                  </div>
                  <div className="producto-item-price">
                    ${(p.precio * item.cantidad).toFixed(2)}
                  </div>
                  <button className="btn btn-danger btn-sm" onClick={() => quitarProducto(item.productoId)}>
                    <Trash2 size={12} />
                  </button>
                </div>
              )
            })}

            {carrito.length > 0 && (
              <div style={{
                display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                marginTop: 12, paddingTop: 12, borderTop: '1px solid var(--border)'
              }}>
                <div style={{ display: 'flex', gap: 16 }}>
                  <span style={{ fontFamily: 'var(--font-mono)', fontSize: 11, color: 'var(--text-muted)' }}>
                    <Clock size={11} style={{ display: 'inline', marginRight: 4 }} />
                    ~{tiempoEstimado}s estimados
                  </span>
                </div>
                <div style={{ fontFamily: 'var(--font-display)', fontSize: 18, fontWeight: 700, color: 'var(--green)' }}>
                  ${totalEstimado.toFixed(2)}
                </div>
              </div>
            )}
          </div>

          <button
            className="btn btn-primary"
            style={{ width: '100%', justifyContent: 'center', padding: '14px' }}
            onClick={procesarCompra}
            disabled={procesando || carrito.length === 0}
          >
            {procesando
              ? <><div className="spinner" style={{ width: 16, height: 16 }} /> Procesando cobro…</>
              : <><Zap size={16} /> Procesar compra</>
            }
          </button>
        </div>

        {/* Columna derecha: simulación visual */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

          {/* Log de simulación */}
          <div className="proceso-card">
            <div className="proceso-header">
              <div className="spinner" style={{ display: procesando ? 'block' : 'none' }} />
              {!procesando && resultado && <CheckCircle size={18} color="var(--green)" />}
              {!procesando && !resultado && <Zap size={18} color="var(--text-muted)" />}
              <span className="proceso-title">
                {procesando ? 'Procesando en hilo asíncrono…' : resultado ? 'Compra completada' : 'Log de simulación'}
              </span>
            </div>
            <div className="proceso-log" ref={logRef}>
              {log.length === 0 && (
                <span style={{ color: 'var(--text-muted)' }}>
                  El log aparecerá aquí cuando inicies una compra…
                </span>
              )}
              {log.map((l, i) => (
                <div key={i} className={`log-line ${l.tipo}`}>
                  <span style={{ color: 'var(--text-muted)', marginRight: 8 }}>[{l.t}]</span>
                  {l.msg}
                </div>
              ))}
            </div>
          </div>

          {/* Resultado final */}
          {resultado && (
            <div className="card" style={{ borderColor: 'rgba(0,255,136,0.3)', animation: 'fadeIn 0.4s ease' }}>
              <div className="card-header">
                <span className="card-title" style={{ color: 'var(--green)' }}>
                  ✅ Compra #{resultado.id} — Procesada
                </span>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                <div>
                  <div className="stat-label">Cliente</div>
                  <div style={{ fontFamily: 'var(--font-body)', fontWeight: 600 }}>
                    {resultado.cliente?.nombre}
                  </div>
                </div>
                <div>
                  <div className="stat-label">Cajera</div>
                  <div style={{ fontFamily: 'var(--font-body)', fontWeight: 600 }}>
                    {resultado.cajera?.nombre}
                  </div>
                </div>
                <div>
                  <div className="stat-label">Total cobrado</div>
                  <div style={{ fontFamily: 'var(--font-display)', fontSize: 24, fontWeight: 800, color: 'var(--green)' }}>
                    ${(resultado.totalCompra || 0).toFixed(2)}
                  </div>
                </div>
                <div>
                  <div className="stat-label">Tiempo total</div>
                  <div style={{ fontFamily: 'var(--font-display)', fontSize: 24, fontWeight: 800, color: 'var(--amber)' }}>
                    {resultado.tiempoTotalProcesamiento}s
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Explicación técnica */}
          <div className="card" style={{ borderColor: 'var(--border)' }}>
            <div className="card-title" style={{ marginBottom: 10, fontSize: 13 }}>¿Cómo funciona la concurrencia?</div>
            <div style={{ fontSize: 12, color: 'var(--text-secondary)', lineHeight: 1.8, fontFamily: 'var(--font-mono)' }}>
              <div>🧵 Cada compra corre en un <span style={{ color: 'var(--green)' }}>hilo independiente</span></div>
              <div>🔒 La cajera se marca <span style={{ color: 'var(--amber)' }}>OCUPADA</span> durante el cobro</div>
              <div>⚡ <span style={{ color: 'var(--blue)' }}>@Async</span> permite múltiples cajeras simultáneas</div>
              <div>⏱  Cada producto suma su <span style={{ color: 'var(--amber)' }}>tiempoProcesamiento</span></div>
              <div>✅ Al terminar la cajera queda <span style={{ color: 'var(--green)' }}>DISPONIBLE</span></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

import React from 'react'
import { CheckCircle, XCircle, Info } from 'lucide-react'

/** Contenedor de notificaciones toast en esquina superior derecha */
export function ToastContainer({ toasts }) {
  if (!toasts.length) return null
  return (
    <div className="toast-container">
      {toasts.map(t => (
        <div key={t.id} className={`toast ${t.type}`}>
          {t.type === 'success' && <CheckCircle size={15} color="var(--green)" />}
          {t.type === 'error'   && <XCircle     size={15} color="var(--red)" />}
          {t.type === 'info'    && <Info         size={15} color="var(--blue)" />}
          {t.message}
        </div>
      ))}
    </div>
  )
}

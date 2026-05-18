import { useState, useCallback } from 'react'

/**
 * Hook para notificaciones tipo toast
 * Uso: const { toasts, toast } = useToast()
 *      toast.success('Cajera creada correctamente')
 *      toast.error('Error al conectar')
 */
export function useToast() {
  const [toasts, setToasts] = useState([])

  const addToast = useCallback((message, type = 'info') => {
    const id = Date.now()
    setToasts(prev => [...prev, { id, message, type }])
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id))
    }, 4000)
  }, [])

  return {
    toasts,
    toast: {
      success: (msg) => addToast(msg, 'success'),
      error:   (msg) => addToast(msg, 'error'),
      info:    (msg) => addToast(msg, 'info'),
    }
  }
}

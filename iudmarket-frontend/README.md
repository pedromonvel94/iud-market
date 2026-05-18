# IUDMarket Frontend
## Sistema de Cobro — Simulación con Concurrencia

Interfaz web para el caso de estudio de simulación de cobro en supermercado.
Desarrollada con React + Vite, conectada al backend Spring Boot.

---

## REQUISITOS PREVIOS

- Node.js 18 o superior (verifica con: node --version)
- pnpm instalado (verifica con: pnpm --version)
  Si no tienes pnpm: npm install -g pnpm
- Backend Spring Boot corriendo en localhost:8080

---

## INSTALACIÓN Y EJECUCIÓN
# 0. Instalar pnpm
npm install -g pnpm

# 1. Instalar dependencias
pnpm install

# 2. Ejecutar en modo desarrollo
pnpm dev

# 3. Abrir en el navegador
http://localhost:3000

---

## ESTRUCTURA DEL PROYECTO

src/
├── components/
│   ├── Sidebar.jsx          ← Navegación lateral
│   └── ToastContainer.jsx   ← Notificaciones
├── hooks/
│   └── useToast.js          ← Hook de notificaciones
├── pages/
│   ├── Dashboard.jsx        ← Métricas generales
│   ├── Cajeras.jsx          ← CRUD de cajeras
│   ├── Clientes.jsx         ← CRUD de clientes
│   ├── Productos.jsx        ← CRUD de productos
│   ├── NuevaCompra.jsx      ← Simulación de cobro (CORE)
│   └── Historial.jsx        ← Historial con detalle
├── services/
│   └── api.js               ← Capa de servicios HTTP (Axios)
├── App.jsx                  ← Componente raíz
├── main.jsx                 ← Punto de entrada
└── index.css                ← Sistema de diseño global

---

## FUNCIONALIDADES

Dashboard     → Métricas en tiempo real (cajeras, clientes, ventas)
Cajeras       → Crear, listar, habilitar/deshabilitar, eliminar
Clientes      → Registrar y gestionar clientes
Productos     → Catálogo con precio y tiempo de procesamiento
Nueva Compra  → Simulación visual del cobro con log en tiempo real
Historial     → Todas las compras con detalle por producto

---

## CONEXIÓN CON EL BACKEND

El proxy de Vite redirige /api → http://localhost:8080
Configurado en vite.config.js — no requiere cambios si el backend
corre en el puerto 8080 por defecto.

Si el backend está en otro puerto, edita vite.config.js:
  target: 'http://localhost:""'

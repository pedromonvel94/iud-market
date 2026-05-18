# IUDMarket 🛒 — Sistema Seguro de Simulación de Cobros

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3+-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue?style=for-the-badge&logo=react)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-5-646CFF?style=for-the-badge&logo=vite)](https://vitejs.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-336791?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![PNPM](https://img.shields.io/badge/pnpm-11-F69220?style=for-the-badge&logo=pnpm)](https://pnpm.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

**IUDMarket** es un sistema full-stack de simulación de cobro en tiempo real para un supermercado, desarrollado con una arquitectura segura y concurrente utilizando hilos independientes (`@Async`) en el backend y una interfaz de usuario premium e interactiva en el frontend.

---

## 📖 Descripción del Proyecto

Este proyecto fue desarrollado para la asignatura de **Desarrollo de Software Seguro** (Semestre 4 - IUDigital). Resuelve el problema de la concurrencia y el procesamiento seguro de transacciones en un entorno transaccional (Supermercado).

### ¿Qué hace la aplicación?
* **Simulación Concurrente**: Cada compra iniciada se procesa en un **hilo asíncrono independiente** (`cajeraExecutor`), simulando de forma realista el tiempo que tarda una cajera física en escanear cada producto en la caja.
* **Control de Estados**: Las cajeras pasan automáticamente a estado `INACTIVA` (ocupadas) cuando están procesando un cobro y regresan a `ACTIVA` (disponibles) al finalizar.
* **Dashboard en Tiempo Real**: Un panel premium con estadísticas en vivo de ventas acumuladas, cajeras disponibles, productos cargados y el historial de compras procesadas.

### ¿Por qué estas tecnologías?
* **Spring Boot (Java 21)**: Proporciona soporte robusto para programación concurrente multinivel, inyección de dependencias limpia e integración segura con JPA/Hibernate.
* **Spring Security**: Implementa autenticación básica HTTP (`HTTP Basic Auth`) protegiendo todos los endpoints de la API, previniendo el acceso no autorizado y manteniendo roles diferenciados (`ADMIN`, `CAJERA`).
* **React + Vite**: Logra una interfaz extremadamente rápida (SPA), modular y con componentes altamente interactivos como logs visuales de procesamiento paso a paso.
* **PostgreSQL**: Motor de base de datos relacional robusto y seguro para garantizar la consistencia e integridad de las transacciones (ACID).

---

## 🗂 Tabla de Contenido
1. [Prerrequisitos](#-prerrequisitos)
2. [Instalación y Configuración del Backend](#-instalación-y-configuración-del-backend)
3. [Instalación y Configuración del Frontend](#-instalación-y-configuración-del-frontend)
4. [Cómo Utilizar la Aplicación (Credenciales)](#-cómo-utilizar-la-aplicación-credenciales)
5. [Pruebas Automatizadas](#-pruebas-automatizadas)
6. [Estructura del Proyecto](#-estructura-del-proyecto)
7. [Contribución](#-contribución)
8. [Licencia](#-licencia)

---

## 🛠 Prerrequisitos

Antes de comenzar, asegúrate de tener instalado:
* **Java Development Kit (JDK) 21** o superior.
* **Node.js** (versión 18 o superior).
* **PostgreSQL** (versión 13 o superior) corriendo localmente.

---

## ⚙️ Instalación y Configuración del Backend

1. **Clonar el repositorio** e ingresar a la carpeta del backend:
   ```bash
   cd iudmarked-backend
   ```

2. **Crear la Base de Datos**:
   Abre tu cliente de PostgreSQL (pgAdmin, DBeaver, o consola psql) y ejecuta:
   ```sql
   CREATE DATABASE iud_market;
   ```

3. **Configurar las Variables de Entorno**:
   El proyecto utiliza variables de entorno para proteger las credenciales del motor de base de datos.
   
   En **Windows (PowerShell)**, define las variables antes de iniciar:
   ```powershell
   $env:DB_USERNAME="tu_usuario_postgres"
   $env:DB_PASSWORD="tu_contraseña_postgres"
   ```
   
   En **Linux/macOS (Terminal)**:
   ```bash
   export DB_USERNAME="tu_usuario_postgres"
   export DB_PASSWORD="tu_contraseña_postgres"
   ```

4. **Ejecutar el Backend**:
   Usa el Maven Wrapper incluido en el proyecto para arrancar el servidor en el puerto `8080`:
   
   * **En Windows**:
     ```powershell
     .\mvnw spring-boot:run
     ```
   * **En Linux/macOS**:
     ```bash
     ./mvnw spring-boot:run
     ```

---

## 💻 Instalación y Configuración del Frontend

1. Abre una **nueva terminal** y navega a la carpeta del frontend:
   ```bash
   cd iudmarket-frontend
   ```

2. **Instalar PNPM de forma global** (si no lo tienes instalado aún):
   ```bash
   npm install -g pnpm
   ```

3. **Instalar dependencias**:
   ```bash
   pnpm install
   ```

4. **Iniciar el servidor de desarrollo**:
   ```bash
   pnpm run dev
   ```
   
   La aplicación se abrirá automáticamente en tu navegador en [http://localhost:3000](http://localhost:3000).

---

## 🔑 Cómo Utilizar la Aplicación (Credenciales)

Al acceder a [http://localhost:3000](http://localhost:3000), el navegador te mostrará una **ventana emergente nativa de inicio de sesión**. Esto es debido al módulo de seguridad HTTP Basic implementado en el backend. 

Usa cualquiera de las siguientes credenciales de desarrollo:

| Rol | Usuario | Contraseña | Permisos |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin` | `admin123` | Control total, CRUD completo de Clientes, Cajeras y Productos. |
| **Cajera** | `cajera` | `cajera123` | Lectura de datos y ejecución de compras. |

### Flujo recomendado para pruebas:
1. Registra un nuevo cliente desde la sección **Clientes**.
2. Asegúrate de tener cajeras configuradas como **ACTIVA** en la sección **Cajeras**.
3. Dirígete a **Nueva Compra**, selecciona el cliente, una cajera disponible, agrega productos al carrito y presiona **Procesar compra**.
4. Podrás observar en tiempo real en la columna derecha cómo se procesa cada producto en un hilo asíncrono seguro.

---

## 🧪 Pruebas Automatizadas

Para garantizar que el flujo de procesamiento de cobros e hilos funcione de forma correcta sin fugas de concurrencia, puedes correr las pruebas automatizadas del backend con el siguiente comando:

* **En Windows**:
  ```powershell
  .\mvnw test
  ```
* **En Linux/macOS**:
  ```bash
  ./mvnw test
  ```

---

## 🏗 Estructura del Proyecto

El repositorio está dividido en una arquitectura desacoplada y limpia:

```text
IUDMarket/
│
├── iudmarked-backend/        # API REST en Spring Boot
│   ├── src/main/java/        # Código fuente (Controladores, Servicios, DAOs, Modelos)
│   ├── src/main/resources/   # Propiedades y configuraciones (application.properties)
│   └── pom.xml               # Dependencias de Maven
│
├── iudmarket-frontend/       # Cliente SPA en React + Vite
│   ├── src/components/       # Componentes visuales reutilizables
│   ├── src/pages/            # Vistas principales (Dashboard, Clientes, Cajeras, etc.)
│   ├── src/services/         # Cliente API HTTP integrado con Axios y Proxy
│   └── package.json          # Script y dependencias de NPM
│
└── README.md                 # Documentación del proyecto
```

---

## 👥 Contribución

¡Las contribuciones son super bienvenidas! Si quieres mejorar el diseño de seguridad o añadir funcionalidades:
1. Haz un **Fork** del proyecto.
2. Crea una rama para tu característica (`git checkout -b feature/NuevaMejora`).
3. Haz un commit con tus cambios (`git commit -m 'Añadida nueva mejora de seguridad'`).
4. Sube la rama (`git push origin feature/NuevaMejora`).
5. Abre un **Pull Request**.

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Para más detalles, consulta el archivo [LICENSE](LICENSE) si está disponible o visita [choosealicense.com](https://choosealicense.com/).

---

*Desarrollado con ❤️ para la Corporación Universitaria Digital de Antioquia - IUDigital.*

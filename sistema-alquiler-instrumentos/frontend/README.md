# Sistema de Instrumentos – Frontend (Camila – semanas 1 a 4)

Interfaz web (SPA) del "Sistema de Control de Inventario y Administración de Alquiler de Instrumentos Musicales"
(UNIFRANZ – Programación 3). **React 18 + Vite + React Router + Leaflet.**

## Qué incluye (según el cronograma)

| Semana | Tarea del cronograma | Dónde está |
|---|---|---|
| 1 | Requerimientos de interfaz y flujos de usuario | `docs/03_requerimientos_de_interfaz.md` |
| 1 | Wireframes del cliente (catálogo, comparación, mapa, verificación) | `docs/wireframes/01_wireframes_cliente.md` |
| 1 | Wireframes del back-office (Encargado y Administrador) | `docs/wireframes/02_wireframes_backoffice.md` |
| 2 | Proyecto SPA (React) y estructura de carpetas | `package.json`, `vite.config.js`, `src/` |
| 2 | Sistema de diseño y componentes base | `src/styles/design-system.css`, `src/components/` |
| 2 | Enrutamiento y estado global | `src/App.jsx` (React Router), `src/context/AppContext.jsx` |
| 3 | Layout general (menú, encabezado, pantallas de carga y error) | `components/Layout.jsx`, `components/Estados.jsx` |
| 3 | Catálogo de instrumentos con filtros (con datos de prueba) | `pages/Catalogo.jsx`, `data/mock.js` |
| 4 | Pantalla de detalle de instrumento | `pages/DetalleInstrumento.jsx` |
| 4 | Catálogo integrado con la API de búsqueda | `services/api.js` → `GET /api/instrumentos/buscar` |
| 4 | Mapa de tiendas cercanas (Leaflet + Geolocation API) | `pages/TiendasCercanas.jsx`, `components/MapaTiendas.jsx` |

## Cómo ejecutarlo

1. Instala **Node.js 18 o superior** (https://nodejs.org, versión LTS).
2. Abre una terminal en esta carpeta (en IntelliJ: *View → Tool Windows → Terminal*) y ejecuta:
   ```
   npm install
   npm run dev
   ```
3. Abre http://localhost:5173

## Conexión con el backend
* Las llamadas a `/api` se reenvían al backend de inventario (Matías) en `http://localhost:8080` (ver `vite.config.js`).
* **Si el backend está apagado, la app usa datos de prueba** y lo avisa con un cartel amarillo y la etiqueta "Datos: prueba" en el encabezado.
* Para forzar los datos de prueba, pon `VITE_USAR_DATOS_PRUEBA=true` en el archivo `.env` y reinicia `npm run dev`.

## Notas
* El mapa usa OpenStreetMap (sin costo de licencia); necesita internet para cargar los mosaicos.
* Si no das permiso de ubicación, las distancias se calculan desde el centro de La Paz.
* El botón "Solicitar alquiler" está deshabilitado: se activa al integrar el módulo de alquileres (semana 6 en adelante).
* Login, roles y paneles llegan en la semana 5; la verificación de identidad en la semana 7.

# Sistema de Control de Inventario y Administración de Alquiler de Instrumentos Musicales

Proyecto grupal – **UNIFRANZ, Programación 3**. Integra en un solo proyecto lo desarrollado hasta la **semana 4** del cronograma:

| Módulo | Responsable | Dónde está |
|---|---|---|
| Inventario, tiendas, búsqueda, comparación de precios y Haversine | Matías Lliulli | `domain`, `application`, `web` (Instrumento, Tienda, Mantenimiento…) |
| Alquileres, devoluciones, penalidades e historial | Leonardo Drew | Cliente, Alquiler, Devolucion, Penalidad… |
| Seguridad: JWT, BCrypt, roles, cifrado AES, HTTPS y bitácora | Ricardo Leyton | `infrastructure/security`, `infrastructure/crypto` |
| Interfaz web: catálogo, detalle, mapa Leaflet | Camila Vargas | carpeta `frontend/` |

* Arquitectura y decisiones de unión: [`docs/arquitectura-unificada.md`](docs/arquitectura-unificada.md)
* **Guía paso a paso** (start.spring.io, IntelliJ, Postman, pgAdmin, GitHub): [`docs/GUIA_PASO_A_PASO.md`](docs/GUIA_PASO_A_PASO.md)

## Estructura
```
sistema-alquiler-instrumentos/
├── pom.xml                      Proyecto Maven (Spring Boot 3.5, Java 17)
├── src/main/java/...            Backend
├── src/main/resources/          application.properties, schema.sql, data.sql, keystore HTTPS
├── src/test/java/...            Pruebas unitarias (JUnit 5 + Mockito)
├── database/                    Scripts SQL para pgAdmin
├── postman/                     Colección única de Postman
├── frontend/                    Aplicación React (Vite)
└── docs/                        Documentación de cada módulo
```

## Arranque rápido
1. Crea en pgAdmin la base vacía `instrumentos_db`.
2. Pon tu clave de PostgreSQL en `src/main/resources/application.properties`.
3. Ejecuta `SistemaAlquilerInstrumentosApplication` → http://localhost:8080/api/ping
4. Importa `postman/Sistema_Alquiler_Instrumentos_UNIDO.postman_collection.json` y ejecuta la carpeta **0** (login).
5. Frontend: `cd frontend` → `npm install` → `npm run dev` → http://localhost:5173

### Usuarios de demostración
| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Admin123*` | ADMINISTRADOR |
| `encargado1` | `Encargado123*` | ENCARGADO |
| `cliente1` | `Cliente123*` | CLIENTE |

> Las claves por defecto (`JWT_SECRETO`, `CIFRADO_CLAVE`, `KEYSTORE_PASSWORD`) y el certificado HTTPS son **solo para desarrollo**.
> Los datos de prueba son ficticios.

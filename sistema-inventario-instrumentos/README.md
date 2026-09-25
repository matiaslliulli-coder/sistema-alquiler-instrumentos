# Sistema de Inventario de Instrumentos Musicales (Matías – semanas 1 a 4)

Módulo **Inventario y Tiendas** del "Sistema de Control de Inventario y Administración de Alquiler de
Instrumentos Musicales" (UNIFRANZ – Programación 3). Spring Boot + JPA + **PostgreSQL**.

## Qué incluye (según el cronograma)

| Semana | Tarea del cronograma | Dónde está |
|---|---|---|
| 1 | Dominio hexagonal Instrumento / Tienda, estados y precio por sucursal | paquete `domain` |
| 2 | Modelado de BD, JPA + PostgreSQL, scripts SQL y datos de prueba | `database/`, `src/main/resources/schema.sql` y `data.sql`, `application.properties` |
| 3 | Modelos JPA de Instrumento, Categoría, Subcategoría y Marca | `domain` |
| 3 | API registrar instrumento | `POST /api/instrumentos` |
| 3 | API registrar instrumento en mantenimiento | `POST /api/mantenimientos` (y `PUT /api/mantenimientos/{id}/cerrar`) |
| 4 | Modelo Tienda + precio por sucursal | `Tienda`, `Instrumento.precioAlquiler` |
| 4 | API buscar instrumentos disponibles (tipo / categoría / ciudad) | `GET /api/instrumentos/buscar` |
| 4 | API comparar precios entre tiendas | `GET /api/instrumentos/comparar-precios` |
| 4 | API sucursales cercanas (Haversine) | `GET /api/tiendas/cercanas` |

## Estructura

```
com.unifranz.sistemainventarioinstrumentos
├── domain                      entidades JPA, enum EstadoInstrumento, HaversineCalculator
├── infrastructure.persistence  repositorios y filtros dinámicos
├── application
│   ├── dto                     requests / responses
│   └── service                 casos de uso
├── web
│   ├── controller              API REST
│   └── exception               errores en JSON
└── config                      CORS para el frontend
```

## Cómo ejecutarlo

1. Instala **JDK 17 o superior** y **PostgreSQL** (con pgAdmin 4).
2. En pgAdmin crea una base de datos vacía llamada `instrumentos_db`
   (clic derecho en *Databases* → *Create* → *Database*, o ejecuta `database/00_crear_base_de_datos.sql`).
3. Abre `src/main/resources/application.properties` y pon tu clave de PostgreSQL en
   `spring.datasource.password` (por defecto `postgres`).
4. Abre el proyecto en IntelliJ IDEA (archivo `pom.xml` → *Open as Project*) y espera que Maven descargue las dependencias.
5. Ejecuta `SistemaInventarioInstrumentosApplication`.
   Al iniciar crea las tablas (`schema.sql`) y carga los datos de prueba (`data.sql`) automáticamente.
6. Prueba en el navegador: http://localhost:8080/api/ping

## Probar con Postman

Importa `postman/Sistema_Inventario_Instrumentos.postman_collection.json`. La variable `baseUrl` es `http://localhost:8080`.

## Notas

* `cod_instrumento` es `VARCHAR(10)` (no `CHAR(10)`) porque en PostgreSQL `CHAR(n)` rellena con espacios.
* `cod_devolucion` e `id_usuario` de `mantenimientos` todavía no tienen clave foránea: esas tablas son de los módulos
  de Alquileres y Seguridad y se enlazarán al integrar el proyecto.
* Los datos de prueba (tiendas, direcciones, teléfonos) son ficticios.

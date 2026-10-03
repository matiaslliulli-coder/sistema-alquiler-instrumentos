> **Nota:** documento original del módulo cuando se trabajaba por separado. En el proyecto unido todo usa el puerto **8080**, la base **instrumentos_db** y el paquete `com.unifranz.sistemaalquilerinstrumentos`. Ver `../README.md` y `GUIA_PASO_A_PASO.md`.

# Sistema de Alquileres de Instrumentos Musicales (Leonardo – semanas 1 a 4)

Módulo **Alquileres** del "Sistema de Control de Inventario y Administración de Alquiler de Instrumentos
Musicales" (UNIFRANZ – Programación 3). Spring Boot + JPA + **PostgreSQL**, Arquitectura Hexagonal.

## Qué incluye (según el cronograma)

| Semana | Tarea del cronograma | Dónde está |
|---|---|---|
| 1 | Requerimientos y casos de uso de alquileres | `docs/01_requerimientos_y_casos_de_uso.md` |
| 1 | Diseño hexagonal: Alquiler, Cliente, Devolución y Penalidad | `docs/02_diseno_hexagonal.md`, paquete `domain` |
| 1 | Diagrama de flujo del ciclo de alquiler | `docs/03_flujo_ciclo_alquiler.md` |
| 2 | Modelado de BD de alquileres (clientes, alquileres, actas, devoluciones, tarifas, penalidades, calificaciones) | `database/01_schema.sql` = `src/main/resources/schema.sql` |
| 2 | Scripts SQL y tarifas de penalidad iniciales | `database/02_datos_prueba.sql` = `src/main/resources/data.sql` |
| 2 | Estructura de paquetes hexagonal en Spring Boot | `src/main/java/...` |
| 3 | Modelos JPA de Cliente, Alquiler y estados del alquiler | `domain` |
| 3 | API: consultar historial de un instrumento | `GET /api/instrumentos/{cod}/historial` |
| 3 | API: registrar alquiler (cliente, instrumento, plazo) | `POST /api/alquileres` |
| 4 | API: registrar devolución y estado de conservación | `POST /api/alquileres/{id}/devolucion` |
| 4 | API: cálculo automático de penalidades (atraso o daño) | `CalculadoraPenalidades` + `DevolucionService` |
| 4 | Pruebas unitarias de alquiler, devolución y penalidades | `src/test/java/...` (18 pruebas) |

## Cómo ejecutarlo

1. Instala **JDK 17 o superior** y **PostgreSQL** (con pgAdmin 4).
2. En pgAdmin crea una base de datos vacía llamada `alquileres_db`
   (clic derecho en *Databases* → *Create* → *Database*, o ejecuta `database/00_crear_base_de_datos.sql`).
3. Abre `src/main/resources/application.properties` y pon tu clave de PostgreSQL en
   `spring.datasource.password` (por defecto `postgres`).
4. Abre el proyecto en IntelliJ IDEA (archivo `pom.xml` → *Open as Project*) y espera que Maven descargue las dependencias.
5. Ejecuta `SistemaAlquileresInstrumentosApplication`. Al iniciar crea las tablas y carga los datos de prueba.
6. Prueba en el navegador: http://localhost:8082/api/ping

> Este módulo usa el puerto **8082** y la base **alquileres_db** para no chocar con el módulo de inventario (8080).

## Probar con Postman
Importa `postman/Sistema_Alquileres_Instrumentos.postman_collection.json` (`baseUrl` = `http://localhost:8082`) y ejecuta las carpetas en orden.

## Pruebas unitarias
En IntelliJ: clic derecho sobre `src/test/java` → *Run 'All Tests'*. O por consola: `mvnw test`.

## Notas
* `instrumentos_ref` es una copia mínima del inventario para trabajar solo; se reemplaza al integrar con el módulo de Matías (semana 10).
* Las tablas `actas` y `calificaciones` ya existen; sus APIs se desarrollan en las semanas 5 y 6.
* Los datos de prueba son ficticios.

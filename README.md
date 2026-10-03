# Sistema de Seguridad de Instrumentos Musicales (Ricardo – semanas 1 a 4)

Módulo **Seguridad e Identidad** del "Sistema de Control de Inventario y Administración de Alquiler de
Instrumentos Musicales" (UNIFRANZ – Programación 3). Spring Boot + Spring Security + JWT + **PostgreSQL**.

## Qué incluye (según el cronograma)

| Semana | Tarea del cronograma | Dónde está |
|---|---|---|
| 1 | Requerimientos de seguridad e identidad | `docs/01_requerimientos_de_seguridad_e_identidad.md` |
| 1 | Arquitectura de seguridad y configuración de la cuenta AWS | `docs/02_arquitectura_de_seguridad.md`, `docs/03_infraestructura_y_despliegue.md` |
| 1 | Diagrama de infraestructura y despliegue | `docs/03_infraestructura_y_despliegue.md` |
| 2 | Repositorios Git/GitHub, ramas y flujo de trabajo | `docs/04_git_y_github.md`, `.gitignore` |
| 2 | Proyecto base Spring Boot con estructura hexagonal | `src/main/java/...` |
| 2 | BD de seguridad e identidad (usuarios, tipos, permisos, consentimientos, verificaciones, intentos fallidos, bitácora) | `database/01_schema.sql` = `src/main/resources/schema.sql` |
| 2 | Configuración base de Spring Security | `infrastructure/security/SecurityConfig` |
| 3 | Autenticación JWT (login y emisión de tokens) | `POST /api/auth/login`, `JwtService` |
| 3 | Cifrado de contraseñas con BCrypt | `SecurityConfig.passwordEncoder()` |
| 3 | RBAC: Administrador, Encargado, Cliente | `SecurityConfig`, `PanelController` |
| 4 | Cifrado en reposo (AES-256-GCM) | `infrastructure/crypto/ServicioCifrado` |
| 4 | HTTPS/TLS y redirección HTTP → HTTPS | `application-https.properties`, `config/HttpsRedirectConfig`, `keystore/` |
| 4 | Bitácora de auditoría (registro inmutable) | `AuditoriaService`, `BitacoraRepository`, reglas SQL |

## Cómo ejecutarlo

1. Instala **JDK 17 o superior** y **PostgreSQL** (con pgAdmin 4).
2. En pgAdmin crea una base de datos vacía llamada `seguridad_db`
   (o ejecuta `database/00_crear_base_de_datos.sql`).
3. En `src/main/resources/application.properties` pon tu clave de PostgreSQL en `spring.datasource.password` (por defecto `postgres`).
4. Abre el proyecto en IntelliJ IDEA (`pom.xml` → *Open as Project*) y espera que Maven descargue las dependencias.
5. Ejecuta `SistemaSeguridadInstrumentosApplication`. Al iniciar crea las tablas, los roles/permisos y **3 usuarios de demostración**.
6. Prueba: http://localhost:8083/api/ping

### Usuarios de demostración
| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Admin123*` | ADMINISTRADOR |
| `encargado1` | `Encargado123*` | ENCARGADO |
| `cliente1` | `Cliente123*` | CLIENTE |

### Modo HTTPS (semana 4)
En IntelliJ: *Run → Edit Configurations → Active profiles* = `https`. Luego abre **https://localhost:8443/api/ping**
(el navegador avisará que el certificado es autofirmado: es normal en desarrollo; *Avanzado → Continuar*).
Si abres `http://localhost:8083/api/ping` te redirige automáticamente a HTTPS.
En Postman desactiva *Settings → SSL certificate verification* y cambia `baseUrl` a `https://localhost:8443`.

## Probar con Postman
Importa `postman/Sistema_Seguridad_Instrumentos.postman_collection.json`. Ejecuta primero los 3 *Login*: el token se guarda solo
en las variables `tokenAdmin`, `tokenEncargado` y `tokenCliente`. Luego prueba los paneles: cada rol debe recibir **200** en
su panel y **403** en los demás.

## Pruebas unitarias
En IntelliJ: clic derecho sobre `src/test/java` → *Run 'All Tests'* (cifrado, login y JWT).

## Notas de seguridad
* Las claves por defecto (`JWT_SECRETO`, `CIFRADO_CLAVE`, `KEYSTORE_PASSWORD`) son **solo para desarrollo**. Defínelas como variables de entorno antes de desplegar.
* El certificado `keystore/sistema-instrumentos.p12` es autofirmado (solo desarrollo). En el despliegue (semana 12) se usa un certificado real.
* Los datos de prueba son ficticios.

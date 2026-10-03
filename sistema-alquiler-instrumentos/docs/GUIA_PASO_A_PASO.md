# Guía paso a paso – Proyecto grupal unido

Repositorio del grupo: <https://github.com/matiaslliulli-coder/sistema-alquiler-instrumentos>

## 0. Qué necesitas instalado
| Programa | Para qué | Versión |
|---|---|---|
| JDK | Ejecutar el backend | 17 o superior |
| IntelliJ IDEA | Abrir y ejecutar el proyecto | Cualquiera (Community sirve) |
| PostgreSQL + pgAdmin 4 | Base de datos | 16 o superior |
| Postman | Probar la API | Última |
| Node.js (LTS) | Solo para el frontend | 18 o superior |
| Git | Subir a GitHub | Última |

---
## 1. Crear el proyecto base en start.spring.io
1. Entra a <https://start.spring.io>.
2. Completa así:

| Campo | Valor |
|---|---|
| Project | **Maven** |
| Language | **Java** |
| Spring Boot | **3.5.x** (la estable que aparezca, sin SNAPSHOT) |
| Group | `com.unifranz` |
| Artifact | `sistema-alquiler-instrumentos` |
| Name | `sistema-alquiler-instrumentos` |
| Description | Sistema de Control de Inventario y Administracion de Alquiler de Instrumentos Musicales |
| Package name | `com.unifranz.sistemaalquilerinstrumentos` |
| Packaging | **Jar** |
| Java | **17** |

3. Botón **ADD DEPENDENCIES** y agrega estas cinco: **Spring Web**, **Spring Data JPA**, **PostgreSQL Driver**, **Validation**, **Spring Security**.
   (La librería JWT no está en esa lista: ya viene escrita en el `pom.xml` del proyecto que te entregué.)
4. Pulsa **GENERATE**: se descarga un `.zip`. Extráelo, por ejemplo en `C:\Proyectos\`.
   Te queda la carpeta `sistema-alquiler-instrumentos` (con `pom.xml`, `src`, `mvnw`…).

## 2. Abrirlo en IntelliJ IDEA
1. IntelliJ → **File → Open…** → elige la carpeta `sistema-alquiler-instrumentos` → **OK** → **Trust Project**.
2. Espera a que abajo termine la barra de progreso (Maven descarga las librerías; la primera vez tarda unos minutos).
3. Verifica **File → Project Structure → Project → SDK** = JDK 17 o superior.

## 3. Poner los archivos del RAR dentro de ese proyecto
1. Extrae `sistema-alquiler-instrumentos_PROYECTO-UNIDO.rar` (WinRAR o 7-Zip). Obtienes otra carpeta `sistema-alquiler-instrumentos`.
2. Cierra IntelliJ (o déjalo abierto, da igual).
3. En la carpeta generada por start.spring.io **borra** `src` y `pom.xml`.
4. Copia desde la carpeta del RAR hacia la carpeta del proyecto: `src`, `pom.xml`, `database`, `postman`, `frontend`, `docs`, `README.md`, `.gitignore` y `.gitattributes`.
   (Deja `mvnw`, `mvnw.cmd` y `.mvn` que generó start.spring.io.)
5. En IntelliJ: clic derecho sobre `pom.xml` → **Maven → Reload project**. Espera a que descargue.
6. Si la carpeta `src/main/java` no aparece en azul: clic derecho sobre `pom.xml` → *Maven → Reload project* otra vez.

> **Atajo:** si prefieres no hacer los pasos 1 a 3, abre directamente la carpeta del RAR con *File → Open* y elige su `pom.xml` (*Open as Project*). Funciona igual.

## 4. Crear la base de datos en pgAdmin
1. Abre **pgAdmin 4** y escribe la contraseña maestra si te la pide.
2. Panel izquierdo: **Servers → PostgreSQL 16** (pon la contraseña de tu usuario `postgres`).
3. Clic derecho en **Databases → Create → Database…**
4. *Database:* `instrumentos_db` → **Save**.
   (Si ya la tenías de antes con tus datos, **no la borres**: se reutiliza y se amplía sola.)

## 5. Configurar la contraseña
Abre `src/main/resources/application.properties` y revisa estas líneas:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME:instrumentos_db}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```
Cambia el último `postgres` (después de `:`) por **tu** contraseña de PostgreSQL.

## 6. Ejecutar el backend
1. Abre `src/main/java/com/unifranz/sistemaalquilerinstrumentos/SistemaAlquilerInstrumentosApplication.java`.
2. Botón verde ▶ junto a `main` → **Run**.
3. En la consola debe aparecer al final: `Started SistemaAlquilerInstrumentosApplication` y `Tomcat started on port 8080`.
4. En el navegador abre <http://localhost:8080/api/ping>. Debe decir `"estado": "OK"`.

Al arrancar, la aplicación **crea las 22 tablas** (`schema.sql`), **carga los datos de prueba** (`data.sql`) y **crea los 3 usuarios** de demostración.

### Si algo falla
| Error en consola | Causa y solución |
|---|---|
| `password authentication failed for user "postgres"` | Contraseña mal puesta en el paso 5. |
| `database "instrumentos_db" does not exist` | Falta el paso 4. |
| `Connection refused` | PostgreSQL apagado: inícialo (Servicios de Windows → postgresql). |
| `Port 8080 was already in use` | Hay otro programa/proyecto usando el 8080: detén el otro proyecto. |
| `release version 17 not supported` | IntelliJ usa un JDK viejo: *File → Project Structure → SDK* = 17+. |
| Símbolos en rojo / `cannot find symbol` | Maven no terminó: *Maven → Reload project* y esperar. |

## 7. Probar en Postman
1. Postman → **Import** → arrastra `postman/Sistema_Alquiler_Instrumentos_UNIDO.postman_collection.json`.
2. **Ejecuta primero la carpeta `0. PRIMERO`** (botón derecho → *Run folder*, o uno por uno). Los tres *Login* guardan solos los tokens `tokenAdmin`, `tokenEncargado` y `tokenCliente`.
3. Luego ejecuta las carpetas **en orden**, una por una:

| Carpeta | Qué prueba | Resultado esperado |
|---|---|---|
| 1–3 | Inventario, mantenimiento, búsqueda, comparar precios, tiendas cercanas | 200 / 201 |
| 4 | Clientes, alquileres, devoluciones, penalidades, historial | 200 / 201; el duplicado da **409** |
| 5 | Seguridad por rol | Cada rol da **200** en su panel y **403** en los demás; sin token **401** |
| 6 | **Flujo integrado** | Alquilar → desaparece del catálogo → devolver dañado (abre mantenimiento) → cerrar → reaparece |
| 7 | Errores del inventario | 400 / 404 |

> Si repites las pruebas, algunos pasos dan 409 (por ejemplo, el instrumento ya está alquilado): es la regla de negocio funcionando, no un error.
> Si alguna vez quieres empezar de cero, ejecuta en pgAdmin `DROP SCHEMA public CASCADE; CREATE SCHEMA public;` sobre `instrumentos_db` y reinicia la app.

## 8. Ver los datos en pgAdmin
1. pgAdmin → **Servers → PostgreSQL → Databases → instrumentos_db → Schemas → public → Tables**: verás las **22 tablas**.
2. Clic derecho sobre una tabla (por ejemplo `alquileres`) → **View/Edit Data → All Rows**.
3. Para consultas: clic derecho sobre `instrumentos_db` → **Query Tool**, abre `database/03_consultas_para_pgadmin.sql`, selecciona una consulta y pulsa **F5**.
4. Haz una prueba: en Postman registra un alquiler (carpeta 4) y en pgAdmin ejecuta la consulta 3: aparece la fila nueva.

Tablas por módulo:
* Inventario: `ciudades, tiendas, categorias, subcategorias, marcas, instrumentos, mantenimientos`
* Seguridad: `tipos_usuario, permisos, tipo_usuario_permiso, usuarios, consentimientos, verificaciones_identidad, intentos_fallidos, bitacora_auditoria`
* Alquileres: `clientes, alquileres, actas, devoluciones, tarifas_penalidad, penalidades, calificaciones`

## 9. Frontend (React)
1. Terminal (en IntelliJ: *View → Tool Windows → Terminal*):
   ```
   cd frontend
   npm install
   npm run dev
   ```
2. Abre <http://localhost:5173> (con el backend encendido muestra datos reales; apagado, usa datos de prueba y avisa).

## 10. Pruebas unitarias
En IntelliJ: clic derecho sobre `src/test/java` → **Run 'All Tests'**. Por consola: `mvnw test`.

## 11. Subir todo a GitHub (rama main)
Antes: avisa al grupo, porque esto reemplaza lo que haya en `main`.

**Con terminal (recomendado)**
```
git clone https://github.com/matiaslliulli-coder/sistema-alquiler-instrumentos.git
cd sistema-alquiler-instrumentos
git checkout main
git pull origin main
```
1. Copia **todo el contenido** de tu proyecto unido (`pom.xml`, `src`, `database`, `postman`, `frontend`, `docs`, `README.md`, `.gitignore`, `.gitattributes`, y `mvnw`, `mvnw.cmd`, `.mvn` si los tienes) dentro de esa carpeta clonada. **No copies** `target`, `.idea` ni `node_modules`.
2. Sube:
```
git status
git add .
git commit -m "feat: integracion de inventario, alquileres, seguridad y frontend"
git push origin main
```
3. En GitHub → **Code**: verás los archivos en `main`.

**Con IntelliJ:** *Git → Manage Remotes…* (agrega el enlace del repo si no está) → *Git → Commit…* (marca los archivos, escribe el mensaje) → **Commit and Push…**.

**Si `main` está protegida o rechaza el push** (`protected branch`): crea una rama y abre un Pull Request.
```
git checkout -b integracion
git add .
git commit -m "feat: integracion de los modulos"
git push -u origin integracion
```
Luego en GitHub pulsa **Compare & pull request** y haz el *Merge*.

**Si te dice `rejected ... fetch first`:** hay cambios nuevos en GitHub. Ejecuta `git pull origin main --rebase` y vuelve a hacer `git push origin main`.

## 12. Qué hacen los demás después
Cada integrante actualiza su copia y trabaja sobre el proyecto unido:
```
git checkout main
git pull origin main
git checkout -b leonardo-alquileres      (o su rama; si ya existe: git checkout leonardo-alquileres && git merge main)
```
Así todos trabajan sobre la misma base (misma BD, mismo puerto 8080).

## 13. Dudas frecuentes
* **¿Por qué ahora el catálogo funciona sin login pero registrar instrumentos pide token?** Es la regla de seguridad: consultar es público; gestionar requiere Encargado o Administrador (tabla en `docs/arquitectura-unificada.md`).
* **¿Cómo uso HTTPS?** En IntelliJ: *Run → Edit Configurations →* campo **Active profiles** = `https`. Abre <https://localhost:8443/api/ping> (el navegador avisa del certificado autofirmado: *Avanzado → Continuar*).
* **¿Dónde cambio las contraseñas de demostración?** En `config/DatosInicialesRunner.java`. Solo se crean si la tabla `usuarios` está vacía.

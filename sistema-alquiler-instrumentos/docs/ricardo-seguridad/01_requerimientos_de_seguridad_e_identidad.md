# Semana 1 – Requerimientos de seguridad e identidad

**Responsable:** Ricardo Ignacio Leyton Cavalcanti

## Requerimientos funcionales
| Código | Requerimiento | Semana |
|---|---|---|
| RS-01 | Autenticación con usuario y contraseña; sesión mediante token JWT. | 3 |
| RS-02 | Contraseñas almacenadas únicamente como hash BCrypt. | 3 |
| RS-03 | Control de acceso por rol (RBAC): Administrador, Encargado de tienda, Cliente. | 3 |
| RS-04 | Cifrado en reposo (AES-256-GCM) de datos biométricos y documentales. | 4 |
| RS-05 | Cifrado del tráfico con HTTPS/TLS y redirección de HTTP a HTTPS. | 4 |
| RS-06 | Bitácora de auditoría inmutable de los eventos del sistema. | 4 |
| RS-07 | Alertas por intentos fallidos de inicio de sesión. | 5 |
| RS-08 | Integración con Amazon Rekognition (Face Liveness) y Amazon Textract. | 6–7 |
| RS-09 | Registro del consentimiento informado antes de capturar datos biométricos. | 6 |
| RS-10 | Gestión de usuarios y roles (API y panel). | 9 |

## Requerimientos no funcionales
* Ningún secreto (clave JWT, clave AES, credenciales AWS) se escribe en el código: se leen de variables de entorno.
* Mensajes de error que no revelan si falló el usuario o la contraseña.
* Registro de IP de origen en intentos fallidos y bitácora.
* Principio de mínimo privilegio en roles de la aplicación y en IAM de AWS.

## Matriz de acceso por rol
| Recurso | Administrador | Encargado | Cliente |
|---|:-:|:-:|:-:|
| Catálogo de instrumentos | ✔ | ✔ | ✔ |
| Registrar/modificar instrumentos | ✔ | ✔ | ✖ |
| Registrar alquileres y devoluciones | ✔ | ✔ | ✖ |
| Verificación de identidad propia | ✖ | ✖ | ✔ |
| Gestión de usuarios y roles | ✔ | ✖ | ✖ |
| Bitácora de auditoría | ✔ | ✖ | ✖ |

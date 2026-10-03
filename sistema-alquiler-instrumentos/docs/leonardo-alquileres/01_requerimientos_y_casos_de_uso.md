# Semana 1 – Requerimientos y casos de uso del módulo de Alquileres

**Responsable:** Leonardo Mateo Drew Urdininea

## Requerimientos funcionales
| Código | Requerimiento |
|---|---|
| RF-A1 | Registrar un alquiler indicando cliente, instrumento y plazo de devolución en días. |
| RF-A2 | Un instrumento solo se puede alquilar si está `DISPONIBLE`; al alquilarlo pasa a `ALQUILADO`. |
| RF-A3 | Registrar clientes (CI, nombres, apellidos, contacto). |
| RF-A4 | Registrar la devolución verificando el estado de conservación (EXCELENTE, BUENO, REGULAR, DANADO). |
| RF-A5 | Calcular automáticamente las penalidades por atraso o daño según las tarifas configuradas. |
| RF-A6 | Consultar el historial de alquileres de un instrumento. |
| RF-A7 | Generar el acta digital (PDF) del alquiler *(semana 5)*. |
| RF-A8 | Notificar al cliente 2 días antes de la devolución *(semana 5)*. |
| RF-A9 | Registrar la calificación de la experiencia del cliente *(semana 6)*. |

## Requerimientos no funcionales
* API REST en JSON, base de datos PostgreSQL, Arquitectura Hexagonal.
* Mensajes de error claros (HTTP 400 / 404 / 409) para el frontend.
* Reglas de negocio probadas con JUnit 5 y Mockito.

## Casos de uso
| Caso de uso | Actor | Flujo principal |
|---|---|---|
| CU-01 Registrar alquiler | Encargado de tienda | Elige cliente e instrumento → indica los días → el sistema valida disponibilidad → calcula fecha prevista y monto → marca el instrumento como ALQUILADO. |
| CU-02 Registrar devolución | Encargado de tienda | Elige el alquiler → indica el estado de conservación → el sistema calcula atraso y penalidades → libera el instrumento (o lo envía a mantenimiento si está DANADO). |
| CU-03 Consultar historial | Encargado / Administrador | Ingresa el código del instrumento → el sistema muestra sus alquileres, devoluciones y penalidades. |
| CU-04 Registrar cliente | Encargado de tienda | Completa el formulario → el sistema valida que el CI no esté repetido. |

## Reglas de negocio
1. Plazo de alquiler: de 1 a 90 días.
2. `monto_total = precio_dia × días`.
3. Atraso: `precio_dia × 0.50 × días_de_atraso` (tarifa configurable en `tarifas_penalidad`).
4. Estado REGULAR → penalidad DANO_LEVE (Bs 50). Estado DANADO → penalidad DANO_GRAVE (Bs 200).
5. Un alquiler solo se devuelve una vez.

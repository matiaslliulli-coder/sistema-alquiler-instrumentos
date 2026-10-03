# Semana 1 – Wireframes del back-office

## Estructura común
```
+---------+----------------------------------------------------+
| MENÚ    |  Encabezado: usuario · rol · [Cerrar sesión]       |
| lateral +----------------------------------------------------+
| Inicio  |                                                    |
| Instrum.|              CONTENIDO DEL PANEL                   |
| Alquil. |                                                    |
| Manten. |                                                    |
| (Admin) |                                                    |
| Usuarios|                                                    |
| Bitácora|                                                    |
+---------+----------------------------------------------------+
```

## Panel de Encargado de Tienda (semanas 5–6)
```
| Instrumentos de mi tienda         [+ Registrar instrumento]  |
| Código     Nombre            Estado        Acciones          |
| INS-000001 Guitarra acúst.   DISPONIBLE    [Alquilar]        |
| INS-000015 Batería           ALQUILADO     [Devolver]        |
| INS-000012 Trompeta          MANTENIMIENTO [Cerrar mant.]    |
```

## Panel de Administrador (semanas 8–9)
```
| Pestañas: [Usuarios y roles] [Tiendas y ciudades] [Tarifas de penalidad] [Bitácora] [Alertas] |
| Usuarios:  Nombre · Usuario · Rol (v) · Activo (☑) · [Guardar]                                 |
```

## Reglas de acceso
* Cliente: solo ve el flujo público y su verificación de identidad.
* Encargado: panel de su tienda (inventario, alquileres, devoluciones, mantenimiento).
* Administrador: todo lo anterior + usuarios, roles, tarifas y bitácora.

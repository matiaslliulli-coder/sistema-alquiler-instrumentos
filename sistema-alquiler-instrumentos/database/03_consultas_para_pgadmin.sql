-- Consultas para VER los datos en pgAdmin (Query Tool sobre la base instrumentos_db).
-- Selecciona una consulta y pulsa F5 (o el boton de ejecutar).

-- 1) Todas las tablas del sistema (deben ser 22)
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public' ORDER BY table_name;

-- 2) Inventario con su tienda, ciudad y estado
SELECT i.cod_instrumento, i.nombre_instrumento, m.nombre_marca, t.nombre_tienda, c.nombre_ciudad,
       i.precio_alquiler, i.estado_instrumento
FROM instrumentos i
JOIN marcas m ON m.cod_marca = i.cod_marca
JOIN tiendas t ON t.cod_tienda = i.cod_tienda
JOIN ciudades c ON c.cod_ciudad = t.cod_ciudad
ORDER BY i.cod_instrumento;

-- 3) Alquileres con cliente e instrumento
SELECT a.cod_alquiler, cl.nombres || ' ' || cl.apellidos AS cliente, a.cod_instrumento,
       a.fecha_alquiler, a.fecha_devolucion_prevista, a.monto_total, a.estado_alquiler
FROM alquileres a JOIN clientes cl ON cl.cod_cliente = a.cod_cliente
ORDER BY a.cod_alquiler;

-- 4) Devoluciones con sus penalidades
SELECT d.cod_devolucion, d.cod_alquiler, d.estado_conservacion, d.dias_atraso,
       p.concepto, p.monto
FROM devoluciones d LEFT JOIN penalidades p ON p.cod_devolucion = d.cod_devolucion
ORDER BY d.cod_devolucion;

-- 5) Mantenimientos (los que vienen de una devolucion con danos tienen cod_devolucion)
SELECT cod_mantenimiento, cod_instrumento, cod_devolucion, estado_mantenimiento, descripcion_dano
FROM mantenimientos ORDER BY cod_mantenimiento;

-- 6) Usuarios y su rol (la contrasena se guarda cifrada con BCrypt)
SELECT u.id_usuario, u.nombre_usuario, t.nombre_tipo AS rol, u.cod_tienda, left(u.password_hash, 12) || '...' AS hash
FROM usuarios u JOIN tipos_usuario t ON t.cod_tipo_usuario = u.cod_tipo_usuario;

-- 7) Bitacora de auditoria (ultimos eventos)
SELECT id_bitacora, fecha, nombre_usuario, accion, detalle, ip_origen
FROM bitacora_auditoria ORDER BY id_bitacora DESC LIMIT 30;

-- 8) Intentos fallidos de inicio de sesion
SELECT * FROM intentos_fallidos ORDER BY fecha_intento DESC;

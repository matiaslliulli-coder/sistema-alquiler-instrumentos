-- =====================================================================
-- Datos iniciales: tipos de usuario, permisos y su asignacion.
-- Los usuarios de demostracion los crea la aplicacion al iniciar
-- (DatosInicialesRunner), porque las contrasenas se cifran con BCrypt.
-- Idempotente: ON CONFLICT DO NOTHING.
-- =====================================================================

INSERT INTO tipos_usuario (nombre_tipo, descripcion) VALUES
    ('ADMINISTRADOR', 'Administra usuarios, roles, tiendas y la bitacora de auditoria'),
    ('ENCARGADO',     'Encargado de tienda: gestiona inventario, alquileres y devoluciones'),
    ('CLIENTE',       'Cliente que consulta el catalogo y alquila instrumentos')
ON CONFLICT (nombre_tipo) DO NOTHING;

INSERT INTO permisos (nombre_permiso, descripcion) VALUES
    ('INVENTARIO_CONSULTAR',  'Consultar el catalogo de instrumentos'),
    ('INVENTARIO_GESTIONAR',  'Registrar y modificar instrumentos'),
    ('ALQUILER_REGISTRAR',    'Registrar alquileres y devoluciones'),
    ('ALQUILER_CONSULTAR',    'Consultar alquileres e historial'),
    ('USUARIOS_GESTIONAR',    'Crear usuarios y asignar roles'),
    ('AUDITORIA_CONSULTAR',   'Consultar la bitacora de auditoria'),
    ('VERIFICACION_IDENTIDAD','Realizar la verificacion de identidad')
ON CONFLICT (nombre_permiso) DO NOTHING;

INSERT INTO tipo_usuario_permiso (cod_tipo_usuario, cod_permiso)
SELECT t.cod_tipo_usuario, p.cod_permiso
FROM (VALUES
    ('ADMINISTRADOR', 'INVENTARIO_CONSULTAR'),
    ('ADMINISTRADOR', 'INVENTARIO_GESTIONAR'),
    ('ADMINISTRADOR', 'ALQUILER_REGISTRAR'),
    ('ADMINISTRADOR', 'ALQUILER_CONSULTAR'),
    ('ADMINISTRADOR', 'USUARIOS_GESTIONAR'),
    ('ADMINISTRADOR', 'AUDITORIA_CONSULTAR'),
    ('ENCARGADO',     'INVENTARIO_CONSULTAR'),
    ('ENCARGADO',     'INVENTARIO_GESTIONAR'),
    ('ENCARGADO',     'ALQUILER_REGISTRAR'),
    ('ENCARGADO',     'ALQUILER_CONSULTAR'),
    ('CLIENTE',       'INVENTARIO_CONSULTAR'),
    ('CLIENTE',       'VERIFICACION_IDENTIDAD')
) AS v(tipo, permiso)
JOIN tipos_usuario t ON t.nombre_tipo = v.tipo
JOIN permisos p ON p.nombre_permiso = v.permiso
ON CONFLICT DO NOTHING;

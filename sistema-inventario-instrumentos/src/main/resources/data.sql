-- =====================================================================
-- Datos de prueba (ficticios). Idempotente: no duplica datos si se
-- ejecuta varias veces.
-- =====================================================================

INSERT INTO ciudades (nombre_ciudad)
SELECT v.nombre
FROM (VALUES ('La Paz'), ('El Alto'), ('Cochabamba'), ('Santa Cruz')) AS v(nombre)
WHERE NOT EXISTS (SELECT 1 FROM ciudades c WHERE c.nombre_ciudad = v.nombre);

INSERT INTO categorias (nombre_categoria)
SELECT v.nombre
FROM (VALUES ('Cuerdas'), ('Viento'), ('Percusión'), ('Teclados')) AS v(nombre)
WHERE NOT EXISTS (SELECT 1 FROM categorias c WHERE c.nombre_categoria = v.nombre);

INSERT INTO subcategorias (cod_categoria, nombre_subcategoria)
SELECT c.cod_categoria, v.sub
FROM (VALUES
    ('Cuerdas', 'Guitarra'),
    ('Cuerdas', 'Violín'),
    ('Cuerdas', 'Charango'),
    ('Viento', 'Flauta'),
    ('Viento', 'Trompeta'),
    ('Viento', 'Quena'),
    ('Percusión', 'Batería'),
    ('Percusión', 'Cajón'),
    ('Teclados', 'Teclado electrónico')
) AS v(cat, sub)
JOIN categorias c ON c.nombre_categoria = v.cat
WHERE NOT EXISTS (
    SELECT 1 FROM subcategorias s
    WHERE s.cod_categoria = c.cod_categoria AND s.nombre_subcategoria = v.sub
);

INSERT INTO marcas (nombre_marca)
SELECT v.nombre
FROM (VALUES ('Yamaha'), ('Fender'), ('Casio'), ('Roland'), ('Pearl'), ('Artesanal')) AS v(nombre)
WHERE NOT EXISTS (SELECT 1 FROM marcas m WHERE m.nombre_marca = v.nombre);

INSERT INTO tiendas (nombre_tienda, cod_ciudad, direccion_tienda, telefono_tienda,
                     latitud_tienda, longitud_tienda, responsable_tienda)
SELECT v.nombre, c.cod_ciudad, v.direccion, v.telefono, v.lat, v.lon, v.responsable
FROM (VALUES
    ('Tienda Centro',      'La Paz',     'Av. Camacho 1234, Zona Central',      '22001111', -16.495800, -68.133500, 'Rodrigo Mamani'),
    ('Tienda Sopocachi',   'La Paz',     'Av. 20 de Octubre 2050, Sopocachi',   '22002222', -16.508300, -68.129000, 'Lucía Quispe'),
    ('Tienda Zona Sur',    'La Paz',     'Calle 21 de Calacoto 8000, Zona Sur', '22003333', -16.539000, -68.083000, 'Mario Choque'),
    ('Tienda El Alto',     'El Alto',    'Av. 6 de Marzo 500, Ciudad Satélite', '28004444', -16.504500, -68.190000, 'Carla Condori'),
    ('Tienda Cochabamba',  'Cochabamba', 'Av. Heroínas 350, Zona Central',      '44005555', -17.393500, -66.157000, 'Jorge Rojas'),
    ('Tienda Santa Cruz',  'Santa Cruz', 'Av. San Martín 700, Equipetrol',      '33006666', -17.783300, -63.182100, 'Ana Suárez')
) AS v(nombre, ciudad, direccion, telefono, lat, lon, responsable)
JOIN ciudades c ON c.nombre_ciudad = v.ciudad
WHERE NOT EXISTS (SELECT 1 FROM tiendas t WHERE t.nombre_tienda = v.nombre);

INSERT INTO instrumentos (cod_instrumento, nombre_instrumento, cod_subcategoria, cod_marca,
                          modelo_instrumento, cod_tienda, precio_alquiler, estado_instrumento)
SELECT v.cod, v.nombre, s.cod_subcategoria, m.cod_marca, v.modelo, t.cod_tienda, v.precio, v.estado
FROM (VALUES
    ('INS-000001', 'Guitarra acústica',  'Guitarra',            'Yamaha',    'C40',         'Tienda Centro',     25.00, 'DISPONIBLE'),
    ('INS-000002', 'Guitarra acústica',  'Guitarra',            'Yamaha',    'C40',         'Tienda Sopocachi',  22.00, 'DISPONIBLE'),
    ('INS-000003', 'Guitarra acústica',  'Guitarra',            'Yamaha',    'C40',         'Tienda Zona Sur',   30.00, 'DISPONIBLE'),
    ('INS-000004', 'Guitarra acústica',  'Guitarra',            'Yamaha',    'C40',         'Tienda El Alto',    20.00, 'DISPONIBLE'),
    ('INS-000005', 'Guitarra eléctrica', 'Guitarra',            'Fender',    'Stratocaster','Tienda Centro',     60.00, 'DISPONIBLE'),
    ('INS-000006', 'Violín 4/4',         'Violín',              'Yamaha',    'V3',          'Tienda Centro',     35.00, 'DISPONIBLE'),
    ('INS-000007', 'Violín 4/4',         'Violín',              'Yamaha',    'V3',          'Tienda Zona Sur',   40.00, 'DISPONIBLE'),
    ('INS-000008', 'Violín 4/4',         'Violín',              'Yamaha',    'V3',          'Tienda Cochabamba', 32.00, 'DISPONIBLE'),
    ('INS-000009', 'Charango',           'Charango',            'Artesanal', 'Concierto',   'Tienda Centro',     18.00, 'DISPONIBLE'),
    ('INS-000010', 'Charango',           'Charango',            'Artesanal', 'Concierto',   'Tienda El Alto',    15.00, 'DISPONIBLE'),
    ('INS-000011', 'Flauta traversa',    'Flauta',              'Yamaha',    'YFL-222',     'Tienda Sopocachi',  28.00, 'DISPONIBLE'),
    ('INS-000012', 'Trompeta',           'Trompeta',            'Yamaha',    'YTR-2330',    'Tienda Centro',     45.00, 'EN MANTENIMIENTO'),
    ('INS-000013', 'Trompeta',           'Trompeta',            'Yamaha',    'YTR-2330',    'Tienda Cochabamba', 42.00, 'DISPONIBLE'),
    ('INS-000014', 'Batería acústica',   'Batería',             'Pearl',     'Roadshow',    'Tienda Zona Sur',   80.00, 'DISPONIBLE'),
    ('INS-000015', 'Batería acústica',   'Batería',             'Pearl',     'Roadshow',    'Tienda Santa Cruz', 75.00, 'ALQUILADO'),
    ('INS-000016', 'Teclado CT-S300',    'Teclado electrónico', 'Casio',     'CT-S300',     'Tienda Centro',     30.00, 'DISPONIBLE'),
    ('INS-000017', 'Teclado CT-S300',    'Teclado electrónico', 'Casio',     'CT-S300',     'Tienda Sopocachi',  27.00, 'DISPONIBLE'),
    ('INS-000018', 'Teclado CT-S300',    'Teclado electrónico', 'Casio',     'CT-S300',     'Tienda Santa Cruz', 28.00, 'DISPONIBLE'),
    ('INS-000019', 'Cajón peruano',      'Cajón',               'Artesanal', 'Estándar',    'Tienda El Alto',    12.00, 'DISPONIBLE'),
    ('INS-000020', 'Cajón peruano',      'Cajón',               'Artesanal', 'Estándar',    'Tienda Centro',     14.00, 'DISPONIBLE'),
    ('INS-000021', 'Quena',              'Quena',               'Artesanal', 'Profesional', 'Tienda Centro',      8.00, 'DISPONIBLE')
) AS v(cod, nombre, subcat, marca, modelo, tienda, precio, estado)
JOIN subcategorias s ON s.nombre_subcategoria = v.subcat
JOIN marcas m ON m.nombre_marca = v.marca
JOIN tiendas t ON t.nombre_tienda = v.tienda
ON CONFLICT (cod_instrumento) DO NOTHING;

-- Un registro de mantenimiento abierto para la trompeta que esta "EN MANTENIMIENTO"
INSERT INTO mantenimientos (cod_instrumento, descripcion_dano, estado_mantenimiento)
SELECT 'INS-000012', 'Pistón trabado y abolladura en la campana', 'EN PROCESO'
WHERE EXISTS (SELECT 1 FROM instrumentos WHERE cod_instrumento = 'INS-000012')
  AND NOT EXISTS (SELECT 1 FROM mantenimientos WHERE cod_instrumento = 'INS-000012');

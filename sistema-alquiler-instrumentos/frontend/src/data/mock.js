// Datos de prueba: reflejan data.sql del modulo de inventario. Se usan cuando el backend no esta disponible.
export const TIENDAS = [
  { codTienda: 1, nombreTienda: 'Tienda Centro', ciudad: 'La Paz', direccionTienda: 'Av. Camacho 1234, Zona Central', telefonoTienda: '22001111', latitudTienda: -16.4958, longitudTienda: -68.1335 },
  { codTienda: 2, nombreTienda: 'Tienda Sopocachi', ciudad: 'La Paz', direccionTienda: 'Av. 20 de Octubre 2050, Sopocachi', telefonoTienda: '22002222', latitudTienda: -16.5083, longitudTienda: -68.129 },
  { codTienda: 3, nombreTienda: 'Tienda Zona Sur', ciudad: 'La Paz', direccionTienda: 'Calle 21 de Calacoto 8000, Zona Sur', telefonoTienda: '22003333', latitudTienda: -16.539, longitudTienda: -68.083 },
  { codTienda: 4, nombreTienda: 'Tienda El Alto', ciudad: 'El Alto', direccionTienda: 'Av. 6 de Marzo 500, Ciudad Satélite', telefonoTienda: '28004444', latitudTienda: -16.5045, longitudTienda: -68.19 },
  { codTienda: 5, nombreTienda: 'Tienda Cochabamba', ciudad: 'Cochabamba', direccionTienda: 'Av. Heroínas 350, Zona Central', telefonoTienda: '44005555', latitudTienda: -17.3935, longitudTienda: -66.157 },
  { codTienda: 6, nombreTienda: 'Tienda Santa Cruz', ciudad: 'Santa Cruz', direccionTienda: 'Av. San Martín 700, Equipetrol', telefonoTienda: '33006666', latitudTienda: -17.7833, longitudTienda: -63.1821 },
];

const T = Object.fromEntries(TIENDAS.map((t) => [t.nombreTienda, t]));

// [cod, nombre, categoria, tipo, marca, modelo, tienda, precio, estado]
const FILAS = [
  ['INS-000001', 'Guitarra acústica', 'Cuerdas', 'Guitarra', 'Yamaha', 'C40', 'Tienda Centro', 25, 'DISPONIBLE'],
  ['INS-000002', 'Guitarra acústica', 'Cuerdas', 'Guitarra', 'Yamaha', 'C40', 'Tienda Sopocachi', 22, 'DISPONIBLE'],
  ['INS-000003', 'Guitarra acústica', 'Cuerdas', 'Guitarra', 'Yamaha', 'C40', 'Tienda Zona Sur', 30, 'DISPONIBLE'],
  ['INS-000004', 'Guitarra acústica', 'Cuerdas', 'Guitarra', 'Yamaha', 'C40', 'Tienda El Alto', 20, 'DISPONIBLE'],
  ['INS-000005', 'Guitarra eléctrica', 'Cuerdas', 'Guitarra', 'Fender', 'Stratocaster', 'Tienda Centro', 60, 'DISPONIBLE'],
  ['INS-000006', 'Violín 4/4', 'Cuerdas', 'Violín', 'Yamaha', 'V3', 'Tienda Centro', 35, 'DISPONIBLE'],
  ['INS-000007', 'Violín 4/4', 'Cuerdas', 'Violín', 'Yamaha', 'V3', 'Tienda Zona Sur', 40, 'DISPONIBLE'],
  ['INS-000008', 'Violín 4/4', 'Cuerdas', 'Violín', 'Yamaha', 'V3', 'Tienda Cochabamba', 32, 'DISPONIBLE'],
  ['INS-000009', 'Charango', 'Cuerdas', 'Charango', 'Artesanal', 'Concierto', 'Tienda Centro', 18, 'DISPONIBLE'],
  ['INS-000010', 'Charango', 'Cuerdas', 'Charango', 'Artesanal', 'Concierto', 'Tienda El Alto', 15, 'DISPONIBLE'],
  ['INS-000011', 'Flauta traversa', 'Viento', 'Flauta', 'Yamaha', 'YFL-222', 'Tienda Sopocachi', 28, 'DISPONIBLE'],
  ['INS-000012', 'Trompeta', 'Viento', 'Trompeta', 'Yamaha', 'YTR-2330', 'Tienda Centro', 45, 'EN MANTENIMIENTO'],
  ['INS-000013', 'Trompeta', 'Viento', 'Trompeta', 'Yamaha', 'YTR-2330', 'Tienda Cochabamba', 42, 'DISPONIBLE'],
  ['INS-000014', 'Batería acústica', 'Percusión', 'Batería', 'Pearl', 'Roadshow', 'Tienda Zona Sur', 80, 'DISPONIBLE'],
  ['INS-000015', 'Batería acústica', 'Percusión', 'Batería', 'Pearl', 'Roadshow', 'Tienda Santa Cruz', 75, 'ALQUILADO'],
  ['INS-000016', 'Teclado CT-S300', 'Teclados', 'Teclado electrónico', 'Casio', 'CT-S300', 'Tienda Centro', 30, 'DISPONIBLE'],
  ['INS-000017', 'Teclado CT-S300', 'Teclados', 'Teclado electrónico', 'Casio', 'CT-S300', 'Tienda Sopocachi', 27, 'DISPONIBLE'],
  ['INS-000018', 'Teclado CT-S300', 'Teclados', 'Teclado electrónico', 'Casio', 'CT-S300', 'Tienda Santa Cruz', 28, 'DISPONIBLE'],
  ['INS-000019', 'Cajón peruano', 'Percusión', 'Cajón', 'Artesanal', 'Estándar', 'Tienda El Alto', 12, 'DISPONIBLE'],
  ['INS-000020', 'Cajón peruano', 'Percusión', 'Cajón', 'Artesanal', 'Estándar', 'Tienda Centro', 14, 'DISPONIBLE'],
  ['INS-000021', 'Quena', 'Viento', 'Quena', 'Artesanal', 'Profesional', 'Tienda Centro', 8, 'DISPONIBLE'],
];

export const INSTRUMENTOS = FILAS.map(([cod, nombre, categoria, tipo, marca, modelo, tienda, precio, estado]) => ({
  codInstrumento: cod,
  nombreInstrumento: nombre,
  categoria,
  tipo,
  marca,
  modeloInstrumento: modelo,
  codTienda: T[tienda].codTienda,
  tienda,
  ciudad: T[tienda].ciudad,
  precioAlquiler: precio,
  estadoInstrumento: estado,
}));

export const CATEGORIAS = ['Cuerdas', 'Viento', 'Percusión', 'Teclados'];
export const CIUDADES = ['La Paz', 'El Alto', 'Cochabamba', 'Santa Cruz'];

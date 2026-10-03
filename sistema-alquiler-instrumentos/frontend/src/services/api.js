import { CATEGORIAS, CIUDADES, INSTRUMENTOS, TIENDAS } from '../data/mock';
import { distanciaKm } from './haversine';

const FORZAR_PRUEBA = import.meta.env.VITE_USAR_DATOS_PRUEBA === 'true';
const sinTildes = (s) => (s || '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();

/** Llama al backend. Si no responde, devuelve los datos de prueba y avisa con modo = 'prueba'. */
async function conRespaldo(real, prueba) {
  if (FORZAR_PRUEBA) return { datos: prueba(), modo: 'prueba' };
  try {
    return { datos: await real(), modo: 'api' };
  } catch (error) {
    if (error.esErrorDeApi) throw error; // el backend respondio con un error (404, 400...): se muestra tal cual
    return { datos: prueba(), modo: 'prueba' }; // backend apagado o sin conexion
  }
}

async function pedir(ruta) {
  const respuesta = await fetch(ruta);
  if (!respuesta.ok) {
    let mensaje = `Error ${respuesta.status}`;
    try {
      mensaje = (await respuesta.json()).mensaje || mensaje;
    } catch { /* cuerpo vacio */ }
    const error = new Error(mensaje);
    error.esErrorDeApi = true;
    throw error;
  }
  return respuesta.json();
}

export function buscarInstrumentos({ tipo, categoria, ciudad, texto, soloDisponibles = true } = {}) {
  const parametros = new URLSearchParams();
  if (tipo) parametros.set('tipo', tipo);
  if (categoria) parametros.set('categoria', categoria);
  if (ciudad) parametros.set('ciudad', ciudad);

  const filtrarTexto = (lista) => {
    const q = sinTildes(texto).trim();
    return q ? lista.filter((i) => sinTildes(`${i.nombreInstrumento} ${i.marca} ${i.modeloInstrumento} ${i.tipo}`).includes(q)) : lista;
  };

  return conRespaldo(
    async () => filtrarTexto(await pedir(`/api/instrumentos/buscar?${parametros}`)),
    () =>
      filtrarTexto(
        INSTRUMENTOS.filter(
          (i) =>
            (!soloDisponibles || i.estadoInstrumento === 'DISPONIBLE') &&
            (!tipo || sinTildes(i.tipo) === sinTildes(tipo)) &&
            (!categoria || sinTildes(i.categoria) === sinTildes(categoria)) &&
            (!ciudad || sinTildes(i.ciudad) === sinTildes(ciudad)),
        ),
      ),
  );
}

export function obtenerInstrumento(cod) {
  return conRespaldo(
    () => pedir(`/api/instrumentos/${encodeURIComponent(cod)}`),
    () => {
      const encontrado = INSTRUMENTOS.find((i) => i.codInstrumento === cod);
      if (!encontrado) {
        const error = new Error(`No existe el instrumento ${cod}`);
        error.esErrorDeApi = true;
        throw error;
      }
      return encontrado;
    },
  );
}

export function compararPrecios(cod) {
  return conRespaldo(
    () => pedir(`/api/instrumentos/${encodeURIComponent(cod)}/comparar-precios`),
    () => {
      const base = INSTRUMENTOS.find((i) => i.codInstrumento === cod);
      const iguales = INSTRUMENTOS.filter(
        (i) => base && i.nombreInstrumento === base.nombreInstrumento && i.marca === base.marca && i.modeloInstrumento === base.modeloInstrumento && i.estadoInstrumento === 'DISPONIBLE',
      ).sort((a, b) => a.precioAlquiler - b.precioAlquiler);
      const precios = iguales.map((i) => i.precioAlquiler);
      return {
        nombreInstrumento: base?.nombreInstrumento,
        marca: base?.marca,
        modelo: base?.modeloInstrumento,
        cantidadTiendas: iguales.length,
        precioMinimo: Math.min(...precios),
        precioMaximo: Math.max(...precios),
        ahorroMaximo: precios.length ? Math.max(...precios) - Math.min(...precios) : 0,
        tiendas: iguales.map((i) => ({ codInstrumento: i.codInstrumento, codTienda: i.codTienda, tienda: i.tienda, ciudad: i.ciudad, precioAlquiler: i.precioAlquiler })),
      };
    },
  );
}

export function tiendasCercanas({ latitud, longitud, tipo, nombre, limite = 6 }) {
  const parametros = new URLSearchParams({ latitud, longitud, limite });
  if (tipo) parametros.set('tipo', tipo);
  if (nombre) parametros.set('nombre', nombre);
  return conRespaldo(
    () => pedir(`/api/tiendas/cercanas?${parametros}`),
    () =>
      TIENDAS.map((t) => {
        const disponibles = INSTRUMENTOS.filter(
          (i) => i.codTienda === t.codTienda && i.estadoInstrumento === 'DISPONIBLE' && (!tipo || sinTildes(i.tipo) === sinTildes(tipo)) && (!nombre || sinTildes(i.nombreInstrumento).includes(sinTildes(nombre))),
        );
        return {
          ...t,
          distanciaKm: Math.round(distanciaKm(latitud, longitud, t.latitudTienda, t.longitudTienda) * 100) / 100,
          cantidadInstrumentosDisponibles: disponibles.length,
          instrumentos: disponibles.map((i) => ({ codInstrumento: i.codInstrumento, nombreInstrumento: i.nombreInstrumento, marca: i.marca, modeloInstrumento: i.modeloInstrumento, precioAlquiler: i.precioAlquiler })),
        };
      })
        .sort((a, b) => a.distanciaKm - b.distanciaKm)
        .slice(0, limite),
  );
}

export function obtenerFiltros() {
  return conRespaldo(
    async () => {
      const [categorias, ciudades] = await Promise.all([pedir('/api/categorias'), pedir('/api/ciudades')]);
      return {
        categorias: categorias.map((c) => c.nombreCategoria),
        tipos: categorias.flatMap((c) => c.subcategorias.map((s) => s.nombreSubcategoria)),
        ciudades: ciudades.map((c) => c.nombreCiudad),
      };
    },
    () => ({ categorias: CATEGORIAS, tipos: [...new Set(INSTRUMENTOS.map((i) => i.tipo))], ciudades: CIUDADES }),
  );
}

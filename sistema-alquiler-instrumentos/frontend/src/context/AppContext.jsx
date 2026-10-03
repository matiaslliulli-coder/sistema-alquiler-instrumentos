import { createContext, useContext, useMemo, useReducer } from 'react';

const AppContext = createContext(null);

const estadoInicial = {
  modoDatos: 'api', // 'api' = backend real, 'prueba' = datos de prueba
  ubicacion: null, // { latitud, longitud } del usuario (Geolocation API)
  filtros: { texto: '', categoria: '', tipo: '', ciudad: '' }, // se conservan al volver desde el detalle
};

function reductor(estado, accion) {
  switch (accion.tipo) {
    case 'MODO_DATOS':
      return estado.modoDatos === accion.modo ? estado : { ...estado, modoDatos: accion.modo };
    case 'UBICACION':
      return { ...estado, ubicacion: accion.ubicacion };
    case 'FILTROS':
      return { ...estado, filtros: { ...estado.filtros, ...accion.filtros } };
    case 'LIMPIAR_FILTROS':
      return { ...estado, filtros: estadoInicial.filtros };
    default:
      return estado;
  }
}

export function AppProvider({ children }) {
  const [estado, dispatch] = useReducer(reductor, estadoInicial);
  const valor = useMemo(() => ({ estado, dispatch }), [estado]);
  return <AppContext.Provider value={valor}>{children}</AppContext.Provider>;
}

export function useApp() {
  const contexto = useContext(AppContext);
  if (!contexto) throw new Error('useApp debe usarse dentro de <AppProvider>');
  return contexto;
}

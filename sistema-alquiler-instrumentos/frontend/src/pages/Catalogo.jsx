import { useCallback, useEffect, useState } from 'react';
import { buscarInstrumentos, obtenerFiltros } from '../services/api';
import { useApp } from '../context/AppContext';
import FiltrosCatalogo from '../components/FiltrosCatalogo';
import TarjetaInstrumento from '../components/TarjetaInstrumento';
import { AvisoModoPrueba, Cargando, MensajeError, SinResultados } from '../components/Estados';

export default function Catalogo() {
  const { estado, dispatch } = useApp();
  const { filtros } = estado;
  const [opciones, setOpciones] = useState({ categorias: [], tipos: [], ciudades: [] });
  const [instrumentos, setInstrumentos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const [intento, setIntento] = useState(0);

  useEffect(() => {
    obtenerFiltros().then(({ datos }) => setOpciones(datos)).catch(() => {});
  }, []);

  const cargar = useCallback(() => {
    let vigente = true;
    setCargando(true);
    setError(null);
    buscarInstrumentos(filtros)
      .then(({ datos, modo }) => {
        if (!vigente) return;
        setInstrumentos(datos);
        dispatch({ tipo: 'MODO_DATOS', modo });
      })
      .catch((e) => vigente && setError(e.message))
      .finally(() => vigente && setCargando(false));
    return () => { vigente = false; };
  }, [filtros, dispatch]);

  useEffect(() => cargar(), [cargar, intento]);

  return (
    <>
      <h1>Catálogo de instrumentos</h1>
      {estado.modoDatos === 'prueba' && <AvisoModoPrueba />}
      <FiltrosCatalogo
        valores={filtros}
        opciones={opciones}
        onCambio={(cambio) => dispatch({ tipo: 'FILTROS', filtros: cambio })}
        onLimpiar={() => dispatch({ tipo: 'LIMPIAR_FILTROS' })}
      />
      {cargando && <Cargando texto="Buscando instrumentos…" />}
      {error && <MensajeError mensaje={error} onReintentar={() => setIntento((n) => n + 1)} />}
      {!cargando && !error && instrumentos.length === 0 && <SinResultados texto="No hay instrumentos disponibles con esos filtros." />}
      {!cargando && !error && instrumentos.length > 0 && (
        <>
          <p style={{ color: 'var(--color-texto-suave)' }}>{instrumentos.length} instrumento(s) disponible(s)</p>
          <div className="cuadricula">
            {instrumentos.map((i) => <TarjetaInstrumento key={i.codInstrumento} instrumento={i} />)}
          </div>
        </>
      )}
    </>
  );
}

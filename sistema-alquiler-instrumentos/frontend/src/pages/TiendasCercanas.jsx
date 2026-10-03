import { useCallback, useEffect, useState } from 'react';
import { tiendasCercanas } from '../services/api';
import { useApp } from '../context/AppContext';
import MapaTiendas from '../components/MapaTiendas';
import { AvisoModoPrueba, Cargando, MensajeError, SinResultados } from '../components/Estados';

const CENTRO_LA_PAZ = { latitud: -16.4958, longitud: -68.1335 }; // se usa si el usuario no da permiso de ubicacion

export default function TiendasCercanas() {
  const { estado, dispatch } = useApp();
  const [tipo, setTipo] = useState('');
  const [tiendas, setTiendas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const [aviso, setAviso] = useState('');
  const [seleccion, setSeleccion] = useState(null);

  const ubicacion = estado.ubicacion;
  const origen = ubicacion ?? CENTRO_LA_PAZ;

  const pedirUbicacion = useCallback(() => {
    if (!navigator.geolocation) {
      setAviso('Tu navegador no permite la geolocalización. Se muestra la distancia desde el centro de La Paz.');
      return;
    }
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        dispatch({ tipo: 'UBICACION', ubicacion: { latitud: pos.coords.latitude, longitud: pos.coords.longitude } });
        setAviso('');
      },
      () => setAviso('No se pudo obtener tu ubicación. Se muestra la distancia desde el centro de La Paz.'),
      { timeout: 8000 },
    );
  }, [dispatch]);

  useEffect(() => {
    if (!ubicacion) pedirUbicacion();
  }, [ubicacion, pedirUbicacion]);

  useEffect(() => {
    let vigente = true;
    setCargando(true);
    setError(null);
    tiendasCercanas({ latitud: origen.latitud, longitud: origen.longitud, tipo, limite: 6 })
      .then(({ datos, modo }) => {
        if (!vigente) return;
        setTiendas(datos);
        dispatch({ tipo: 'MODO_DATOS', modo });
      })
      .catch((e) => vigente && setError(e.message))
      .finally(() => vigente && setCargando(false));
    return () => { vigente = false; };
  }, [origen.latitud, origen.longitud, tipo, dispatch]);

  return (
    <>
      <h1>Tiendas cercanas</h1>
      {estado.modoDatos === 'prueba' && <AvisoModoPrueba />}
      {aviso && <div className="aviso-prueba">{aviso}</div>}
      <div className="tarjeta fila" style={{ marginBottom: 16 }}>
        <div className="campo">
          <label htmlFor="tipo">Tipo de instrumento (opcional)</label>
          <input id="tipo" placeholder="Guitarra, Violín, Charango…" value={tipo} onChange={(e) => setTipo(e.target.value)} />
        </div>
        <button className="btn btn-secundario" onClick={pedirUbicacion}>📍 Usar mi ubicación</button>
      </div>
      {error && <MensajeError mensaje={error} />}
      <MapaTiendas tiendas={tiendas} ubicacion={ubicacion} onSeleccionar={setSeleccion} />
      {cargando && <Cargando texto="Calculando distancias…" />}
      {!cargando && !error && tiendas.length === 0 && <SinResultados texto="No hay tiendas con ese tipo de instrumento." />}
      <div className="cuadricula" style={{ marginTop: 16 }}>
        {tiendas.map((t) => (
          <article key={t.codTienda} className="tarjeta" style={seleccion === t.codTienda ? { borderColor: 'var(--color-acento)', borderWidth: 2 } : undefined}>
            <h3>{t.nombreTienda}</h3>
            <p style={{ margin: '0 0 6px', color: 'var(--color-texto-suave)' }}>{t.direccionTienda}<br />{t.ciudad} · ☎ {t.telefonoTienda}</p>
            <span className="insignia insignia-neutra">{t.distanciaKm} km</span>{' '}
            <span className={`insignia ${t.cantidadInstrumentosDisponibles > 0 ? 'insignia-ok' : 'insignia-error'}`}>{t.cantidadInstrumentosDisponibles} disponible(s)</span>
          </article>
        ))}
      </div>
    </>
  );
}

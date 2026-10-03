import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { compararPrecios, obtenerInstrumento } from '../services/api';
import { useApp } from '../context/AppContext';
import EstadoInstrumento from '../components/EstadoInstrumento';
import { AvisoModoPrueba, Cargando, MensajeError } from '../components/Estados';

export default function DetalleInstrumento() {
  const { cod } = useParams();
  const { estado, dispatch } = useApp();
  const [instrumento, setInstrumento] = useState(null);
  const [comparacion, setComparacion] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let vigente = true;
    setCargando(true);
    setError(null);
    Promise.all([obtenerInstrumento(cod), compararPrecios(cod).catch(() => null)])
      .then(([principal, comparar]) => {
        if (!vigente) return;
        setInstrumento(principal.datos);
        setComparacion(comparar?.datos ?? null);
        dispatch({ tipo: 'MODO_DATOS', modo: principal.modo });
      })
      .catch((e) => vigente && setError(e.message))
      .finally(() => vigente && setCargando(false));
    return () => { vigente = false; };
  }, [cod, dispatch]);

  if (cargando) return <Cargando texto="Cargando instrumento…" />;
  if (error) return (<><MensajeError mensaje={error} /><p><Link to="/catalogo">← Volver al catálogo</Link></p></>);

  const i = instrumento;
  return (
    <>
      {estado.modoDatos === 'prueba' && <AvisoModoPrueba />}
      <p className="migas"><Link to="/catalogo">← Catálogo</Link> / {i.codInstrumento}</p>
      <div className="dos-columnas">
        <section className="tarjeta">
          <EstadoInstrumento estado={i.estadoInstrumento} />
          <h1 style={{ marginTop: 8 }}>{i.nombreInstrumento}</h1>
          <table className="tabla">
            <tbody>
              <tr><th>Código</th><td>{i.codInstrumento}</td></tr>
              <tr><th>Marca</th><td>{i.marca}</td></tr>
              <tr><th>Modelo</th><td>{i.modeloInstrumento}</td></tr>
              <tr><th>Tipo</th><td>{i.tipo} ({i.categoria})</td></tr>
              <tr><th>Tienda</th><td>{i.tienda}, {i.ciudad}</td></tr>
            </tbody>
          </table>
          <div className="precio" style={{ marginTop: 16 }}>Bs {Number(i.precioAlquiler).toFixed(2)} <small>/ día</small></div>
          <button className="btn btn-primario" style={{ marginTop: 12 }} disabled title="El alquiler en línea se habilita en semanas posteriores">
            Solicitar alquiler (próximamente)
          </button>
        </section>

        <section className="tarjeta">
          <h2>Comparar precios entre tiendas</h2>
          {!comparacion || comparacion.tiendas.length === 0 ? (
            <p style={{ color: 'var(--color-texto-suave)' }}>No hay otras tiendas con este mismo instrumento.</p>
          ) : (
            <>
              <p style={{ color: 'var(--color-texto-suave)' }}>
                {comparacion.cantidadTiendas} tienda(s) · ahorro máximo <strong>Bs {Number(comparacion.ahorroMaximo).toFixed(2)}</strong> por día
              </p>
              <table className="tabla">
                <thead><tr><th>Tienda</th><th>Ciudad</th><th>Precio/día</th></tr></thead>
                <tbody>
                  {comparacion.tiendas.map((t) => (
                    <tr key={t.codInstrumento}>
                      <td><Link to={`/instrumentos/${t.codInstrumento}`}>{t.tienda}</Link></td>
                      <td>{t.ciudad}</td>
                      <td><strong>Bs {Number(t.precioAlquiler).toFixed(2)}</strong>{Number(t.precioAlquiler) === Number(comparacion.precioMinimo) && <> <span className="insignia insignia-ok">más barato</span></>}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </>
          )}
        </section>
      </div>
    </>
  );
}

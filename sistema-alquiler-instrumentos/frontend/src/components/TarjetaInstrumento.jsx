import { Link } from 'react-router-dom';
import EstadoInstrumento from './EstadoInstrumento';

export default function TarjetaInstrumento({ instrumento: i }) {
  return (
    <article className="tarjeta">
      <EstadoInstrumento estado={i.estadoInstrumento} />
      <h3 style={{ marginTop: 8 }}>{i.nombreInstrumento}</h3>
      <p style={{ margin: '0 0 8px', color: 'var(--color-texto-suave)' }}>
        {i.marca} · {i.modeloInstrumento}
        <br />
        {i.tipo} ({i.categoria})
        <br />
        📍 {i.tienda}, {i.ciudad}
      </p>
      <div className="precio">Bs {Number(i.precioAlquiler).toFixed(2)} <small>/ día</small></div>
      <Link className="btn btn-secundario" style={{ marginTop: 12 }} to={`/instrumentos/${i.codInstrumento}`}>Ver detalle</Link>
    </article>
  );
}

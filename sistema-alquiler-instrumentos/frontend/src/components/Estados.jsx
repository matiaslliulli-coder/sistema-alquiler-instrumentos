import { Link } from 'react-router-dom';

export function Cargando({ texto = 'Cargando…' }) {
  return (
    <div className="estado" role="status">
      <div className="spinner" />
      {texto}
    </div>
  );
}

export function MensajeError({ mensaje, onReintentar }) {
  return (
    <div className="mensaje-error" role="alert">
      <strong>Ocurrió un problema.</strong> {mensaje}
      {onReintentar && (
        <div style={{ marginTop: 12 }}>
          <button className="btn btn-secundario" onClick={onReintentar}>Reintentar</button>
        </div>
      )}
    </div>
  );
}

export function SinResultados({ texto = 'No se encontraron resultados.' }) {
  return <div className="estado">{texto}</div>;
}

export function AvisoModoPrueba() {
  return (
    <div className="aviso-prueba">
      Mostrando <strong>datos de prueba</strong>: el servidor de inventario (puerto 8080) no respondió. Inicia el backend y recarga para ver los datos reales.
    </div>
  );
}

export function PaginaNoEncontrada() {
  return (
    <div className="estado">
      <h2>404 – Página no encontrada</h2>
      <p>La dirección que buscas no existe.</p>
      <Link className="btn btn-primario" to="/">Volver al inicio</Link>
    </div>
  );
}

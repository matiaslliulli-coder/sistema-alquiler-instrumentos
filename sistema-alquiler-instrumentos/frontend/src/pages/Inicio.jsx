import { Link } from 'react-router-dom';

export default function Inicio() {
  return (
      <div className="fondo-imagen">
      <section className="tarjeta" style={{ padding: 32, marginBottom: 16 }}>
        <h1>Alquila el instrumento que necesitas</h1>
        <p style={{ fontSize: '1.05rem', color: 'var(--color-texto-suave)' }}>
          Compara precios entre tiendas, encuentra la sucursal más cercana y reserva guitarras, violines, charangos, teclados y más.
        </p>
        <div className="fila">
          <Link className="btn btn-primario" to="/catalogo">Ver catálogo</Link>
          <Link className="btn btn-secundario" to="/tiendas">Tiendas cercanas</Link>
        </div>
      </section>
      <section className="cuadricula">
        {[
          ['🔎', 'Busca fácil', 'Filtra por categoría, tipo y ciudad.'],
          ['💸', 'Compara precios', 'Mira cuánto cuesta el mismo instrumento en cada tienda.'],
          ['🗺️', 'Encuentra tu tienda', 'El mapa te muestra las sucursales más cercanas a ti.'],
        ].map(([icono, titulo, texto]) => (
          <div className="tarjeta" key={titulo}>
            <div style={{ fontSize: '1.8rem' }}>{icono}</div>
            <h3>{titulo}</h3>
            <p style={{ margin: 0, color: 'var(--color-texto-suave)' }}>{texto}</p>
          </div>
        ))}
      </section>
      </div>
  );
}

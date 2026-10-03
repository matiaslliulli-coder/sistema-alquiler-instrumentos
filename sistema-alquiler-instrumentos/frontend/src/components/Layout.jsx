import { NavLink, Outlet, Link } from 'react-router-dom';
import { useApp } from '../context/AppContext';

const enlaces = [
  { a: '/', texto: 'Inicio', fin: true },
  { a: '/catalogo', texto: 'Catálogo' },
  { a: '/tiendas', texto: 'Tiendas cercanas' },
];

export default function Layout() {
  const { estado } = useApp();
  return (
    <div className="app">
      <header className="encabezado">
        <div className="encabezado-interno">
          <Link to="/" className="marca">🎸 Sistema de Instrumentos</Link>
          <span className="modo" title="De dónde vienen los datos">
            {estado.modoDatos === 'api' ? 'Datos: servidor' : 'Datos: prueba'}
          </span>
          <nav className="menu" aria-label="Menú principal">
            {enlaces.map((e) => (
              <NavLink key={e.a} to={e.a} end={e.fin} className={({ isActive }) => (isActive ? 'activo' : '')}>
                {e.texto}
              </NavLink>
            ))}
          </nav>
        </div>
      </header>
      <main className="contenido">
        <Outlet />
      </main>
      <footer className="pie">UNIFRANZ · Sistema de Control de Inventario y Alquiler de Instrumentos Musicales</footer>
    </div>
  );
}

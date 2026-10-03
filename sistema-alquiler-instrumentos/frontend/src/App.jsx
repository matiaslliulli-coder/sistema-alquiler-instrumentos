import { Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import { PaginaNoEncontrada } from './components/Estados';
import Inicio from './pages/Inicio';
import Catalogo from './pages/Catalogo';
import DetalleInstrumento from './pages/DetalleInstrumento';
import TiendasCercanas from './pages/TiendasCercanas';

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Inicio />} />
        <Route path="catalogo" element={<Catalogo />} />
        <Route path="instrumentos/:cod" element={<DetalleInstrumento />} />
        <Route path="tiendas" element={<TiendasCercanas />} />
        <Route path="*" element={<PaginaNoEncontrada />} />
      </Route>
    </Routes>
  );
}

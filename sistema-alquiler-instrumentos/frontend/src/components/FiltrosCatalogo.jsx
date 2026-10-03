export default function FiltrosCatalogo({ valores, opciones, onCambio, onLimpiar }) {
  const campo = (nombre, etiqueta, lista) => (
    <div className="campo">
      <label htmlFor={nombre}>{etiqueta}</label>
      <select id={nombre} value={valores[nombre]} onChange={(e) => onCambio({ [nombre]: e.target.value })}>
        <option value="">Todas</option>
        {lista.map((o) => <option key={o} value={o}>{o}</option>)}
      </select>
    </div>
  );
  return (
    <div className="tarjeta fila" style={{ marginBottom: 16 }}>
      <div className="campo" style={{ flex: 2 }}>
        <label htmlFor="texto">Buscar</label>
        <input id="texto" type="search" placeholder="Guitarra, Yamaha, violín…" value={valores.texto} onChange={(e) => onCambio({ texto: e.target.value })} />
      </div>
      {campo('categoria', 'Categoría', opciones.categorias)}
      {campo('tipo', 'Tipo', opciones.tipos)}
      {campo('ciudad', 'Ciudad', opciones.ciudades)}
      <button className="btn btn-secundario" onClick={onLimpiar}>Limpiar</button>
    </div>
  );
}

const CLASES = { DISPONIBLE: 'insignia-ok', ALQUILADO: 'insignia-aviso', 'EN MANTENIMIENTO': 'insignia-error' };

export default function EstadoInstrumento({ estado }) {
  return <span className={`insignia ${CLASES[estado] || 'insignia-neutra'}`}>{estado}</span>;
}

import { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import iconoUrl from 'leaflet/dist/images/marker-icon.png';
import icono2xUrl from 'leaflet/dist/images/marker-icon-2x.png';
import sombraUrl from 'leaflet/dist/images/marker-shadow.png';

// Vite no resuelve solo las rutas de los iconos de Leaflet: se indican a mano.
const iconoTienda = L.icon({ iconUrl: iconoUrl, iconRetinaUrl: icono2xUrl, shadowUrl: sombraUrl, iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34] });
const iconoUsuario = L.divIcon({ className: '', html: '<div style="width:16px;height:16px;border-radius:50%;background:#1f6feb;border:3px solid #fff;box-shadow:0 0 0 2px #1f6feb"></div>', iconSize: [16, 16], iconAnchor: [8, 8] });

/** Mapa interactivo (Leaflet + OpenStreetMap) con las tiendas y, si existe, la ubicacion del usuario. */
export default function MapaTiendas({ tiendas, ubicacion, onSeleccionar }) {
  const contenedor = useRef(null);
  const mapa = useRef(null);
  const capa = useRef(null);

  useEffect(() => {
    mapa.current = L.map(contenedor.current).setView([-16.5, -68.15], 11);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    }).addTo(mapa.current);
    capa.current = L.layerGroup().addTo(mapa.current);
    return () => {
      mapa.current.remove();
      mapa.current = null;
    };
  }, []);

  useEffect(() => {
    if (!mapa.current) return;
    capa.current.clearLayers();
    const puntos = [];
    tiendas.forEach((t) => {
      const punto = [Number(t.latitudTienda), Number(t.longitudTienda)];
      puntos.push(punto);
      const distancia = t.distanciaKm != null ? `<br/>A ${t.distanciaKm} km de ti` : '';
      L.marker(punto, { icon: iconoTienda })
        .bindPopup(`<strong>${t.nombreTienda}</strong><br/>${t.direccionTienda ?? ''}${distancia}<br/>${t.cantidadInstrumentosDisponibles ?? 0} instrumento(s) disponible(s)`)
        .on('click', () => onSeleccionar?.(t.codTienda))
        .addTo(capa.current);
    });
    if (ubicacion) {
      const punto = [ubicacion.latitud, ubicacion.longitud];
      puntos.push(punto);
      L.marker(punto, { icon: iconoUsuario }).bindPopup('Tu ubicación').addTo(capa.current);
    }
    if (puntos.length > 1) mapa.current.fitBounds(L.latLngBounds(puntos), { padding: [40, 40], maxZoom: 14 });
    else if (puntos.length === 1) mapa.current.setView(puntos[0], 14);
  }, [tiendas, ubicacion, onSeleccionar]);

  return <div ref={contenedor} className="mapa" aria-label="Mapa de tiendas" />;
}

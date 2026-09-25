package com.unifranz.sistemainventarioinstrumentos.domain;

/**
 * Calculo de la distancia entre dos puntos de la Tierra con la formula de Haversine.
 * Se usa para encontrar las sucursales mas cercanas al cliente.
 */
public final class HaversineCalculator {

    public static final double RADIO_TIERRA_KM = 6371.0088;

    private HaversineCalculator() {
    }

    /** Devuelve la distancia en kilometros entre (lat1, lon1) y (lat2, lon2), en grados decimales. */
    public static double distanciaKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double sinDLat = Math.sin(dLat / 2);
        double sinDLon = Math.sin(dLon / 2);
        double a = sinDLat * sinDLat
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * sinDLon * sinDLon;
        a = Math.min(1.0, Math.max(0.0, a));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c;
    }
}

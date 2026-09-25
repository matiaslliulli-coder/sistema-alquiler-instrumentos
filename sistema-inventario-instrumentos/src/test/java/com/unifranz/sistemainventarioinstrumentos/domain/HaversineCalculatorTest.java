package com.unifranz.sistemainventarioinstrumentos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HaversineCalculatorTest {

    @Test
    void mismoPuntoTieneDistanciaCero() {
        assertEquals(0.0, HaversineCalculator.distanciaKm(-16.4958, -68.1335, -16.4958, -68.1335), 0.0001);
    }

    @Test
    void ungradoDeLongitudEnElEcuadorSonUnos111Km() {
        assertEquals(111.19, HaversineCalculator.distanciaKm(0, 0, 0, 1), 0.05);
    }

    @Test
    void laDistanciaEsSimetrica() {
        double ida = HaversineCalculator.distanciaKm(-16.4958, -68.1335, -17.3935, -66.1570);
        double vuelta = HaversineCalculator.distanciaKm(-17.3935, -66.1570, -16.4958, -68.1335);
        assertEquals(ida, vuelta, 0.0001);
    }
}

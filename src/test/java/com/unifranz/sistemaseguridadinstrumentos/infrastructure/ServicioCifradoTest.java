package com.unifranz.sistemaseguridadinstrumentos.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.unifranz.sistemaseguridadinstrumentos.infrastructure.crypto.ServicioCifrado;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class ServicioCifradoTest {

    // 32 bytes en Base64 (clave AES-256 de prueba)
    private static final String CLAVE = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    private final ServicioCifrado servicio = new ServicioCifrado(CLAVE);

    @Test
    void cifrarYDescifrar_devuelveElDatoOriginal() {
        byte[] original = "Carnet 7654321 LP - Mariana Quispe".getBytes(StandardCharsets.UTF_8);

        byte[] cifrado = servicio.cifrar(original);

        assertThat(cifrado).isNotEqualTo(original);
        assertThat(servicio.descifrar(cifrado)).isEqualTo(original);
    }

    @Test
    void elMismoDatoCifradoDosVecesDaResultadosDistintos_porqueElIvEsAleatorio() {
        byte[] dato = "dato biometrico".getBytes(StandardCharsets.UTF_8);

        assertThat(servicio.cifrar(dato)).isNotEqualTo(servicio.cifrar(dato));
    }

    @Test
    void cifrarTextoYDescifrarTexto_conTildesYEnies() {
        String texto = "Ni\u00f1o de La Paz: cobran Bs 50 por da\u00f1o";

        assertThat(servicio.descifrarTexto(servicio.cifrarTexto(texto))).isEqualTo(texto);
    }

    @Test
    void siSeAlteraUnByteDelDatoCifrado_elDescifradoFalla() {
        byte[] cifrado = servicio.cifrar("dato".getBytes(StandardCharsets.UTF_8));
        cifrado[cifrado.length - 1] ^= 0x01;

        assertThatThrownBy(() -> servicio.descifrar(cifrado)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void conUnaClaveDistinta_noSePuedeDescifrar() {
        byte[] otraClave = new byte[32];
        Arrays.fill(otraClave, (byte) 7);
        ServicioCifrado otro = new ServicioCifrado(Base64.getEncoder().encodeToString(otraClave));
        byte[] cifrado = servicio.cifrar("secreto".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> otro.descifrar(cifrado)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void laClaveDebeTener32Bytes() {
        String corta = Base64.getEncoder().encodeToString(new byte[16]);

        assertThatThrownBy(() -> new ServicioCifrado(corta))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("32 bytes");
    }

    @Test
    void laClaveDebeEstarEnBase64() {
        assertThatThrownBy(() -> new ServicioCifrado("###no-es-base64###"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void descifrarUnDatoDemasiadoCorto_lanzaError() {
        assertThatThrownBy(() -> servicio.descifrar(new byte[5])).isInstanceOf(IllegalArgumentException.class);
    }
}

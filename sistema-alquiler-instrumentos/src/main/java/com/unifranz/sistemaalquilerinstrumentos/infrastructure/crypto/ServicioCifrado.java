package com.unifranz.sistemaalquilerinstrumentos.infrastructure.crypto;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Cifrado en reposo con AES-256-GCM (cifrado autenticado).
 * Formato del resultado: [IV de 12 bytes][texto cifrado + etiqueta de autenticacion de 16 bytes].
 * Se usa para datos biometricos y documentales (imagenes del rostro, datos del carnet).
 */
@Component
public class ServicioCifrado {

    private static final int LONGITUD_IV = 12;
    private static final int LONGITUD_TAG_BITS = 128;

    private final SecretKey clave;
    private final SecureRandom aleatorio = new SecureRandom();

    public ServicioCifrado(@Value("${app.cifrado.clave}") String claveBase64) {
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(claveBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La clave de cifrado debe estar en Base64", e);
        }
        if (bytes.length != 32) {
            throw new IllegalArgumentException("La clave de cifrado debe tener 32 bytes (AES-256), tiene " + bytes.length);
        }
        this.clave = new SecretKeySpec(bytes, "AES");
    }

    public byte[] cifrar(byte[] datos) {
        try {
            byte[] iv = new byte[LONGITUD_IV];
            aleatorio.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, clave, new GCMParameterSpec(LONGITUD_TAG_BITS, iv));
            byte[] cifrado = cipher.doFinal(datos);

            byte[] resultado = new byte[iv.length + cifrado.length];
            System.arraycopy(iv, 0, resultado, 0, iv.length);
            System.arraycopy(cifrado, 0, resultado, iv.length, cifrado.length);
            return resultado;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo cifrar el dato", e);
        }
    }

    public byte[] descifrar(byte[] datosCifrados) {
        if (datosCifrados == null || datosCifrados.length <= LONGITUD_IV) {
            throw new IllegalArgumentException("El dato cifrado no es valido");
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, clave, new GCMParameterSpec(LONGITUD_TAG_BITS, datosCifrados, 0, LONGITUD_IV));
            return cipher.doFinal(datosCifrados, LONGITUD_IV, datosCifrados.length - LONGITUD_IV);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo descifrar: dato alterado o clave incorrecta", e);
        }
    }

    public String cifrarTexto(String texto) {
        return Base64.getEncoder().encodeToString(cifrar(texto.getBytes(StandardCharsets.UTF_8)));
    }

    public String descifrarTexto(String textoCifradoBase64) {
        return new String(descifrar(Base64.getDecoder().decode(textoCifradoBase64)), StandardCharsets.UTF_8);
    }
}

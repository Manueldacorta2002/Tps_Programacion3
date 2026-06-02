package com.utn.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para hashear contraseñas con SHA-256.
 *
 * NOTA ACADÉMICA: SHA-256 sin salt se usa aquí con fines educativos
 * para demostrar el concepto de hashing de contraseñas.
 * En un sistema productivo se debe usar BCrypt, Argon2 o PBKDF2
 * que incluyen salt y son resistentes a ataques de fuerza bruta.
 */
public class PasswordEncoderUtil {

    private PasswordEncoderUtil() {
        // Clase utilitaria, no instanciar
    }

    /**
     * Hashea una contraseña en texto plano con SHA-256.
     *
     * @param plainPassword contraseña en texto plano
     * @return hash hexadecimal de la contraseña
     */
    public static String encode(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible en esta JVM", e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado.
     *
     * @param plainPassword  contraseña en texto plano ingresada
     * @param hashedPassword hash almacenado en la base de datos
     * @return true si coinciden
     */
    public static boolean matches(String plainPassword, String hashedPassword) {
        return encode(plainPassword).equals(hashedPassword);
    }
}

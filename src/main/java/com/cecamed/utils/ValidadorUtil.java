package com.cecamed.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValidadorUtil {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static boolean esEmailValido(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(regex);
    }

    public static boolean esCedulaValida(String cedula) {
        return cedula != null && cedula.length() >= 8 && cedula.length() <= 15;
    }

    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && telefono.matches("^[0-9\\-\\+\\s()]+$") && telefono.length() >= 7;
    }

    public static String formatearFecha(LocalDate fecha) {
        return fecha != null ? fecha.format(FORMATO_FECHA) : "";
    }

    public static String formatearFechaHora(LocalDateTime fechaHora) {
        return fechaHora != null ? fechaHora.format(FORMATO_DATETIME) : "";
    }

    public static boolean esContraseñaFuerte(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }
        boolean tieneMayuscula = contrasena.matches(".*[A-Z].*");
        boolean tieneMinuscula = contrasena.matches(".*[a-z].*");
        boolean tieneNumero = contrasena.matches(".*[0-9].*");
        return tieneMayuscula && tieneMinuscula && tieneNumero;
    }
}

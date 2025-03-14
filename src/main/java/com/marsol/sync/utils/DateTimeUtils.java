package com.marsol.sync.utils;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {
    private static final DateTimeFormatter FORMATTER_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_YY = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");

    public static LocalDateTime stringToDateTime(String value) {
        try {
            // Intentar parsear con el formato de 4 dígitos en el año (yyyy)
            return LocalDateTime.parse(value, FORMATTER_YYYY);
        } catch (DateTimeParseException e1) {
            try {
                // Si falla, intentar con el formato de 2 dígitos en el año (yy)
                LocalDateTime date = LocalDateTime.parse(value, FORMATTER_YY);

                // Si el año es menor a 100, ajustarlo para que caiga en el rango correcto
                if (date.getYear() < 100) {
                    date = date.withYear(2000 + date.getYear());
                }

                return date;
            } catch (DateTimeParseException e2) {
                System.out.println("Error parsing date, skipping: " + value);
                return null;
            }
        }
    }

    public static void main(String[] args) {
        String fecha1 = "25-02-25 14:26:22";  // Con yy (debe interpretarse como 2025)
        String fecha2 = "25-02-2025 14:26:22"; // Con yyyy

        System.out.println("Fecha 1: " + stringToDateTime(fecha1));
        System.out.println("Fecha 2: " + stringToDateTime(fecha2));
    }
}

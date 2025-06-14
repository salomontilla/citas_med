package com.salomon.citasmedbackend.infra.utils;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FechaUtils {
    public static Date convertirFecha(String fechaString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(fechaString, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa dd-MM-yyyy");
        }
    }

    public static Time convertirHora(String horaString) {
        try {
            return Time.valueOf(horaString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de hora inválido. Usa HH:mm:ss");
        }
    }
}

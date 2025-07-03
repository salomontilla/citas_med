package com.salomon.citasmedbackend.infra.utils;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FechaUtils {
    public static Date convertirFecha(String fechaString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(fechaString, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa yyyy-MM-dd");
        }
    }

    public static Time convertirHora(String horaString) {
        try {
            return Time.valueOf(horaString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de hora inválido. Usa HH:mm:ss");
        }
    }

    public static List<String> dividirEnBloques(LocalTime inicio, LocalTime fin) {
        List<String> bloques = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!inicio.isAfter(fin.minusMinutes(30))) {
            bloques.add(inicio.format(formatter));
            inicio = inicio.plusMinutes(30);
        }

        return bloques;
    }
}

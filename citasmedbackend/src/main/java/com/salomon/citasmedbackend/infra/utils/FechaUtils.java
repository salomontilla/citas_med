package com.salomon.citasmedbackend.infra.utils;

import com.salomon.citasmedbackend.domain.disponibilidad.DiaSemana;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
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
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
          LocalTime localTime = LocalTime.parse(horaString, formatter);
          return Time.valueOf(localTime);
      } catch (DateTimeParseException e) {
          throw new IllegalArgumentException("Formato de hora inválido. Usa HH:mm");
      }
  }


    public static List<String> dividirEnBloquesDisponibles(LocalTime inicio, LocalTime fin) {
        List<String> bloques = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!inicio.isAfter(fin.minusMinutes(30))) {
            bloques.add(inicio.format(formatter));
            inicio = inicio.plusMinutes(30);
        }

        return bloques;
    }

    public static DiaSemana convertirADiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
            }
}

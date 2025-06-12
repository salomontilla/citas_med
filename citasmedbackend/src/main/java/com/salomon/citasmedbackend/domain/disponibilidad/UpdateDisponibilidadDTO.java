package com.salomon.citasmedbackend.domain.disponibilidad;

public record UpdateDisponibilidadDTO(
        DiaSemana diaSemana,
        String horaInicio,
        String horaFin
) {
}

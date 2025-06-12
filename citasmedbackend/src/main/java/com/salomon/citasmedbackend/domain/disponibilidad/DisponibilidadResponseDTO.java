package com.salomon.citasmedbackend.domain.disponibilidad;

public record DisponibilidadResponseDTO(
        Long id,
        Long MedicoId,
        String diaSemana,
        String horaInicio,
        String horaFin) {
}

package com.salomon.citasmedbackend.domain.cita;


public record CitaActualizarDTO(
        String fecha,
        String hora,
        EstadoCita estado
) {
}

package com.salomon.citasmedbackend.domain.cita;

public record CitaResponseDTO (
    Long id,
    Long pacienteId,
    Long medicoId,
    String fecha,
    String hora,
    String estado
) {

}

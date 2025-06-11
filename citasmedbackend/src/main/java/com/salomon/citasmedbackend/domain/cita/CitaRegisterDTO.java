package com.salomon.citasmedbackend.domain.cita;

import jakarta.validation.constraints.NotBlank;


public record CitaRegisterDTO(
    @NotBlank
    Long pacienteId,
    @NotBlank
    Long medicoId,
    @NotBlank
    String fecha,
    @NotBlank
    String hora
) {
}

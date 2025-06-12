package com.salomon.citasmedbackend.domain.cita;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CitaActualizarDTO(
        @NotBlank
        String fecha,
        @NotBlank
        String hora,
        @NotNull
        EstadoCita estado
) {
}

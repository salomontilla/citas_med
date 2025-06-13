package com.salomon.citasmedbackend.domain.disponibilidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DisponibilidadDTO(

    @NotNull
    DiaSemana diaSemana,
    @NotBlank
    String horaInicio,
    @NotBlank
    String horaFin
) {
}

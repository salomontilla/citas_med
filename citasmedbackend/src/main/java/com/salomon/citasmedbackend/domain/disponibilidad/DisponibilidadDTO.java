package com.salomon.citasmedbackend.domain.disponibilidad;

import com.salomon.citasmedbackend.domain.medico.DiaSemana;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DisponibilidadDTO(
    @NotNull
    Long medicoId,
    @NotNull
    DiaSemana diaSemana,
    @NotBlank
    String horaInicio,
    @NotBlank
    String horaFin
) {
}

package com.salomon.citasmedbackend.domain.cita;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CitaAgendarDTO(

    @NotNull
    Long medicoId,
    @NotBlank
    String fecha,
    @NotBlank
    String hora
) {
}

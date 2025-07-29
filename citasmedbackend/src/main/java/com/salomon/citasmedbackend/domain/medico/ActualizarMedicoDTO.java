package com.salomon.citasmedbackend.domain.medico;

import jakarta.validation.constraints.Email;

public record ActualizarMedicoDTO(
        String nombreCompleto,
        @Email
        String email,
        String documento,
        String contrasena,
        String telefono
) {
}

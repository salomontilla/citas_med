package com.salomon.citasmedbackend.domain.medico;

public record ActualizarMedicoDTO(
        String nombreCompleto,
        String email,
        String documento,
        String contrasena,
        String telefono
) {
}

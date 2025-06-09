package com.salomon.citasmedbackend.domain.medico;

public record RegistrarMedicoDTO(
        String nombreCompleto,
        String email,
        String contrasena,
        String documento,
        String telefono,
        String especialidad
) {
}

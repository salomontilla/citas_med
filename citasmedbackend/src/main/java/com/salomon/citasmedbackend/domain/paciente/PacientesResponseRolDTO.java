package com.salomon.citasmedbackend.domain.paciente;

public record PacientesResponseRolDTO(
        String nombreCompleto,
        String documento,
        String email,
        String telefono,
        String fechaNacimiento,
        String rol
) {
}

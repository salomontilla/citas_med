package com.salomon.citasmedbackend.domain.medico;

public record ActualizarMedicoAdminDTO(
        String nombreCompleto,
        String email,
        String documento,
        String contrasena,
        String telefono,
        String especialidad
) {
}

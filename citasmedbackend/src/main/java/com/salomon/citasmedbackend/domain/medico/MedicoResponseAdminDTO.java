package com.salomon.citasmedbackend.domain.medico;

public record MedicoResponseAdminDTO(
        Long id,
        String nombre,
        String email,
        String documento,
        String telefono,
        boolean isActivo,
        String especialidad
) {
}

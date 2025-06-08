package com.salomon.citasmedbackend.domain.medico;

public record MedicoResponseDTO(
        Long id,
        String nombre,
        String email,
        String documento,
        String telefono,
        String especialidad
) {
}

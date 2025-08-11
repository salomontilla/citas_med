package com.salomon.citasmedbackend.domain.paciente;

import com.salomon.citasmedbackend.domain.usuario.Usuario;

import java.sql.Date;

public record PacientesResponseDTO(
        Long id,
        String nombreCompleto,
        String documento,
        String email,
        String telefono,
        Date fechaNacimiento,
        boolean isActivo
) {
}

package com.salomon.citasmedbackend.domain.paciente;

import java.sql.Date;

public record PacientesResponseAdminDTO (
        Long id,
        String nombreCompleto,
        String documento,
        String email,
        String telefono,
        Boolean activo,
        Date fechaNacimiento
) {
}

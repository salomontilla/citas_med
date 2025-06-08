package com.salomon.citasmedbackend.domain.usuario;

import com.salomon.citasmedbackend.domain.paciente.Paciente;

import java.sql.Date;

public record PacientesResponseDTO(
        Long id,
        String nombreCompleto,
        String documento,
        String email,
        String telefono,
        Date fechaNacimiento
) {
    public static PacientesResponseDTO fromPaciente(Paciente paciente) {
        Usuario u = paciente.getUsuario();
        return new PacientesResponseDTO(
                paciente.getId(),
                u.getNombreCompleto(),
                u.getDocumento(),
                u.getEmail(),
                u.getTelefono(),
                paciente.getFechaNacimiento()
        );
    }
}

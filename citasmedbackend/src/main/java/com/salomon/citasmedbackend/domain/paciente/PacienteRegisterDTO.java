package com.salomon.citasmedbackend.domain.paciente;

public record PacienteRegisterDTO(
    Long id,
    String nombreCompleto,
    String email,
    String contrasena,
    String telefono,
    String documento,
    String fechaNacimiento
) {
}

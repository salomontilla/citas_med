package com.salomon.citasmedbackend.domain.paciente;

public record PacienteUpdateDTO(
    String nombreCompleto,
    String email,
    String contrasena,
    String telefono,
    String documento,
    String fechaNacimiento
) {
}

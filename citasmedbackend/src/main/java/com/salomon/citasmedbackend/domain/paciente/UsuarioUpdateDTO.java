package com.salomon.citasmedbackend.domain.paciente;

public record UsuarioUpdateDTO(
    String nombreCompleto,
    String email,
    String contrasena,
    String telefono,
    String documento,
    String fechaNacimiento
) {
}

package com.salomon.citasmedbackend.domain.usuario;

public record UsuarioRegisterDTO (
    String nombreCompleto,
    String email,
    String contrasena,
    String telefono,
    String documento,
    String fechaNacimiento
) {
}

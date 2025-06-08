package com.salomon.citasmedbackend.domain.usuario;

public record UserResponseDTO(
    Long id,
    String nombreCompleto,
    String email,
    String telefono,
    String documento,
    boolean activo,
    String rol
) {
}

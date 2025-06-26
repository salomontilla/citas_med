package com.salomon.citasmedbackend.domain.usuario;

public record DetallesUsuarioResponseDTO(
        String nombre,
        String email,
        String rol
) {
}

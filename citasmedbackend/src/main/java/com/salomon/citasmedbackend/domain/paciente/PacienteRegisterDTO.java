package com.salomon.citasmedbackend.domain.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PacienteRegisterDTO(
    @NotNull
    Long id,
    @NotBlank
    String nombreCompleto,
    @NotBlank
    @Email
    String email,
    @NotBlank
    String contrasena,
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener exactamente 10 dígitos")
    String telefono,
    @NotBlank
    @Pattern(regexp = "\\d{1,10}", message = "El documento debe tener máximo 10 dígitos")
    String documento,
    @NotBlank
    String fechaNacimiento
) {
}

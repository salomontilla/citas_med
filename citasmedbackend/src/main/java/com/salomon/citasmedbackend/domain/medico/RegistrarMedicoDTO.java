package com.salomon.citasmedbackend.domain.medico;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrarMedicoDTO(
        @NotBlank
        String nombreCompleto,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String contrasena,

        @NotBlank
        @Pattern(regexp = "\\d{1,10}", message = "El documento debe tener máximo 10 dígitos")
        String documento,

        @NotBlank
        @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener exactamente 10 dígitos")
        String telefono,

        @NotBlank
        String especialidad
) {
}

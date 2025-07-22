package com.salomon.citasmedbackend.domain.cita;

public record CitaMedicosResponseDTO
        (Long id,
         Long idMedico,
         String nombrePaciente,
         String documento,
         String fecha,
         String hora,
         String estado) {
}

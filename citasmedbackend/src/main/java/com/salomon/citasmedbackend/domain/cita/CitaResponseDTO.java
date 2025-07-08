package com.salomon.citasmedbackend.domain.cita;

import com.salomon.citasmedbackend.domain.medico.Especialidad;

public record CitaResponseDTO (
    Long id,
    Long idMedico,
    String nombreMedico,
    Especialidad especialidad,
    String fecha,
    String hora,
    String estado
) {

}

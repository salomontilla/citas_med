package com.salomon.citasmedbackend.domain.disponibilidad;

import java.util.List;

public record BloqueDisponibilidadDTO(
        Long id,
        Long MedicoId,
        String diaSemana,
        List<String> bloques
) {
}

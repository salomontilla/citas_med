package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadDTO;
import com.salomon.citasmedbackend.domain.medico.Disponibilidad;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.infra.utils.FechaUtils;
import com.salomon.citasmedbackend.repository.DisponibilidadRepository;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.salomon.citasmedbackend.infra.utils.FechaUtils.convertirHora;

@Service
@RequiredArgsConstructor
public class DisponibilidadService {
    private final MedicoRepository medicoRepository;
    private final DisponibilidadRepository disponibilidadRepository;

    public Disponibilidad agregarDisponibilidad(DisponibilidadDTO dto) {
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(dto.medicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));


        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setMedico(medico);
        disponibilidad.setDiaSemana(dto.diaSemana());
        disponibilidad.setHoraInicio(convertirHora(dto.horaInicio()));
        disponibilidad.setHoraFin(convertirHora(dto.horaFin()));

        return disponibilidadRepository.save(disponibilidad);
    }
}

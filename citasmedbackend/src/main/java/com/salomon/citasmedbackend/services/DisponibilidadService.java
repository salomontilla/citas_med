package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadDTO;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.repository.DisponibilidadRepository;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

import static com.salomon.citasmedbackend.infra.utils.FechaUtils.convertirHora;

@Service
@RequiredArgsConstructor
public class DisponibilidadService {
    private final MedicoRepository medicoRepository;
    private final DisponibilidadRepository disponibilidadRepository;

    public Disponibilidad agregarDisponibilidad(DisponibilidadDTO dto) {
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(dto.medicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));
        LocalTime horaInicio = convertirHora(dto.horaInicio()).toLocalTime();
        LocalTime horaFin = convertirHora(dto.horaFin()).toLocalTime();

        // 1. Validar que la hora de inicio sea antes que la hora fin
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        // 2. Validar que no se cruce con una disponibilidad ya existente
        List<Disponibilidad> existentes = disponibilidadRepository
                .findByMedicoIdAndDiaSemana(medico.getId(), dto.diaSemana());

        boolean seCruza = existentes.stream().anyMatch(d ->
                horaInicio.isBefore(d.getHoraFin().toLocalTime()) &&
                        horaFin.isAfter(d.getHoraInicio().toLocalTime())
        );

        if (seCruza) {
            throw new IllegalArgumentException("Ya existe una disponibilidad para este médico que se cruza con la nueva.");
        }


        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setMedico(medico);
        disponibilidad.setDiaSemana(dto.diaSemana());
        disponibilidad.setHoraInicio(convertirHora(dto.horaInicio()));
        disponibilidad.setHoraFin(convertirHora(dto.horaFin()));

        return disponibilidadRepository.save(disponibilidad);
    }

    public List<Disponibilidad> obtenerDisponibilidades(){
        return disponibilidadRepository.findAll();
    }

    public List<Disponibilidad> obtenerDisponibilidadesPorMedico(Long medicoId) {
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));
        return disponibilidadRepository.findByMedicoId(medico.getId());
    }

    public String eliminarDisponibilidad(Long id) {
        Disponibilidad disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
        disponibilidadRepository.delete(disponibilidad);
        return "Disponibilidad eliminada correctamente.";
    }

}

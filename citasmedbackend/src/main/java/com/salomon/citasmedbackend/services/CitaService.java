package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaActualizarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaAgendarDTO;
import com.salomon.citasmedbackend.domain.cita.EstadoCita;
import com.salomon.citasmedbackend.domain.disponibilidad.DiaSemana;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.repository.CitaRepository;
import com.salomon.citasmedbackend.repository.DisponibilidadRepository;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import com.salomon.citasmedbackend.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.salomon.citasmedbackend.infra.utils.FechaUtils.convertirFecha;
import static com.salomon.citasmedbackend.infra.utils.FechaUtils.convertirHora;

@Service
@RequiredArgsConstructor
public class CitaService {
    private final DisponibilidadRepository diponibilidadRepository;
    private final CitaRepository citasRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    private static final Map<DayOfWeek, DiaSemana> DIA_SEMANA_MAP = Map.of(
            DayOfWeek.MONDAY, DiaSemana.LUNES,
            DayOfWeek.TUESDAY, DiaSemana.MARTES,
            DayOfWeek.WEDNESDAY, DiaSemana.MIERCOLES,
            DayOfWeek.THURSDAY, DiaSemana.JUEVES,
            DayOfWeek.FRIDAY, DiaSemana.VIERNES,
            DayOfWeek.SATURDAY, DiaSemana.SABADO,
            DayOfWeek.SUNDAY, DiaSemana.DOMINGO
    );

    public Cita agendarCita( CitaAgendarDTO citaDto) {
        Paciente paciente = pacienteRepository.findByIdAndUsuarioActivo(citaDto.pacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(citaDto.medicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));

        Date formattedDate = convertirFecha(citaDto.fecha());
        Time formattedTime = convertirHora(citaDto.hora());
        validarDisponibilidad(medico.getId(), formattedDate, formattedTime);
        Cita nuevaCita = new Cita(
                paciente,
                medico,
                formattedDate,
                formattedTime,
                EstadoCita.PENDIENTE
        );

        citasRepository.save(nuevaCita);
        return nuevaCita;
    }

    public List<Cita> obtenerCitas() {
        List<Cita> citas = citasRepository.findAll();
        if (citas.isEmpty()) {
            return List.of();
        }
        return citas;
    }

    public Cita obtenerCitaById(Long id) {
        return citasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    public List<Cita> obtenerCitasPorPacienteId(Long pacienteId) {
        Paciente paciente = pacienteRepository.findByIdAndUsuarioActivo(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));
        return citasRepository.findByPacienteId(paciente.getId());

    }

    public List<Cita> obtenerCitasPorMedicoId(Long medicoId) {
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));
        return citasRepository.findByMedicoId(medico.getId());
    }

   public Cita actualizarCita(Long id, CitaActualizarDTO citaDto) {
            Cita cita = citasRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Cita no encontrada")
            );
            if (citaDto.fecha() != null) {
                cita.setFecha(convertirFecha(citaDto.fecha()));
            }
            if (citaDto.hora() != null) {
                cita.setHora(convertirHora(citaDto.hora()));
            }
            if (citaDto.estado() != null) {
                cita.setEstado(citaDto.estado());
            }

            citasRepository.save(cita);
            return cita;
   }

    public String cancelarCita(Long id) {
        Cita cita = citasRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cita no encontrada")
        );

        if (cita.getEstado() == EstadoCita.CANCELADA) {
            return "La cita ya está cancelada.";
        }

        cita.setEstado(EstadoCita.CANCELADA);
        citasRepository.save(cita);
        return "Cita cancelada correctamente.";
    }

    private void validarDisponibilidad(Long medicoId, Date fechaAgendada, Time horaInicio) {
        DiaSemana dia = DIA_SEMANA_MAP.get(fechaAgendada.toLocalDate().getDayOfWeek());
        List<Disponibilidad> disponibilidad = diponibilidadRepository
                .findDisponibilidadByMedicoIdAndDiaSemana(medicoId, dia);
        if (disponibilidad.isEmpty()) {
            throw new RuntimeException("No hay disponibilidad para el médico en este día");
        }

        LocalTime horaInicioDeseada = horaInicio.toLocalTime();
        LocalTime horaFin = horaInicioDeseada.plusMinutes(30);

        boolean dentroDeHorario = disponibilidad.stream().anyMatch(d ->
                !horaInicioDeseada.isBefore(d.getHoraInicio().toLocalTime()) &&
                        !horaFin.isAfter(d.getHoraFin().toLocalTime())
        );

        if (!dentroDeHorario) {
            throw new RuntimeException("La hora está fuera del horario de atención del médico");
        }
        // Validar que no se cruce con otra cita
        List<Cita> citasEseDia = citasRepository.findByMedicoIdAndFecha(medicoId, fechaAgendada);

        for (Cita cita : citasEseDia) {
            LocalTime citaInicio = cita.getHora().toLocalTime();
            LocalTime citaFin = citaInicio.plusMinutes(30);

            // Verificar si se cruzan (hay solapamiento)
            if (!(horaFin.isBefore(citaInicio) || horaInicioDeseada.isAfter(citaFin.minusNanos(1)))) {
                throw new RuntimeException("Ya hay una cita agendada en ese horario");
            }
        }
    }
}

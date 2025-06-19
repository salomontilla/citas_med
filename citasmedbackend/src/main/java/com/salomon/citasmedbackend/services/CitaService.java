package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.cita.*;
import com.salomon.citasmedbackend.domain.disponibilidad.DiaSemana;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private final UserRepository usuarioRepository;


    private static final Map<DayOfWeek, DiaSemana> DIA_SEMANA_MAP = Map.of(
            DayOfWeek.MONDAY, DiaSemana.LUNES,
            DayOfWeek.TUESDAY, DiaSemana.MARTES,
            DayOfWeek.WEDNESDAY, DiaSemana.MIERCOLES,
            DayOfWeek.THURSDAY, DiaSemana.JUEVES,
            DayOfWeek.FRIDAY, DiaSemana.VIERNES,
            DayOfWeek.SATURDAY, DiaSemana.SABADO,
            DayOfWeek.SUNDAY, DiaSemana.DOMINGO
    );

    public Cita agendarCita( CitaAgendarDTO citaDto, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("Paciente encontrado: " + usuario.getId());
        Paciente paciente = pacienteRepository.findPacienteByUsuarioIdActivo(usuario.getId())
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

    public Page<Cita> obtenerCitas(Pageable pageable) {
        Page<Cita> citas = citasRepository.findAll(pageable);
        if (citas.isEmpty()) {
            return Page.empty();
        }
        return citas;
    }

    public Cita obtenerCitaById(Long id) {
        return citasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    public Page<Cita> obtenerCitasPorPacienteId(Long pacienteId, Pageable pageable) {
        Paciente paciente = pacienteRepository.findByIdAndUsuarioActivo(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));
        return citasRepository.findByPacienteId(paciente.getId(), pageable);

    }

    public Page<Cita> obtenerCitasPorMedicoId(Long medicoId, Pageable pageable) {
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));
        return citasRepository.findByMedicoId(medico.getId(), pageable);
    }

   public Cita actualizarCitaAdmin(Long id, CitaActualizarDTO citaDto) {
            Cita cita = citasRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Cita no encontrada")
            );
       return validatedCitaAdmin(citaDto, cita);
   }

    public Cita actualizarCitaPaciente(Long id, CitaPacienteActualizarDTO citaDto, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = pacienteRepository.findPacienteByUsuarioIdActivo(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));

        Cita cita = citasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!cita.getPaciente().getId().equals(paciente.getId())) {
            throw  new RuntimeException("No tienes permiso para editar esta cita");
        }
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("No se puede editar una cita cancelada");
        }
        return validatedCitaPaciente(citaDto, cita);
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

    private Cita validatedCitaPaciente(CitaPacienteActualizarDTO dto, Cita cita) {
        Date nuevaFecha = dto.fecha() != null ? convertirFecha(dto.fecha()) : cita.getFecha();
        Time nuevaHora = dto.hora() != null ? convertirHora(dto.hora()) : cita.getHora();

        validarDisponibilidad(cita.getMedico().getId(), nuevaFecha, nuevaHora);

        if (dto.fecha() != null) cita.setFecha(nuevaFecha);
        if (dto.hora() != null) cita.setHora(nuevaHora);

        return citasRepository.save(cita);
    }

    private Cita validatedCitaAdmin(CitaActualizarDTO citaDto, Cita cita) {
        Date nuevaFecha = citaDto.fecha() != null ? convertirFecha(citaDto.fecha()) : cita.getFecha();
        Time nuevaHora = citaDto.hora() != null ? convertirHora(citaDto.hora()) : cita.getHora();

        validarDisponibilidad(cita.getMedico().getId(), nuevaFecha, nuevaHora);

        if (citaDto.fecha() != null) {
            cita.setFecha(nuevaFecha);
        }
        if (citaDto.hora() != null) {
            cita.setHora(nuevaHora);
        }
        if (citaDto.estado() != null) {
            cita.setEstado(citaDto.estado());
        }

        return citasRepository.save(cita);
    }

    private void validarDisponibilidad(Long medicoId, Date fechaAgendada, Time horaInicio) {
        DiaSemana dia = DIA_SEMANA_MAP.get(fechaAgendada.toLocalDate().getDayOfWeek());

        LocalDate fecha = fechaAgendada.toLocalDate();
        if (!fecha.isAfter(LocalDate.now()) && !fecha.isEqual(LocalDate.now())) {
            throw new RuntimeException("No se pueden agendar citas en fechas pasadas");
        }

        List<Disponibilidad> disponibilidad = diponibilidadRepository
                .findDisponibilidadByMedicoIdAndDiaSemana(medicoId, dia);
        if (disponibilidad.isEmpty()) {
            throw new RuntimeException("No hay disponibilidad para el médico en este día");
        }

        LocalTime horaInicioDeseada = horaInicio.toLocalTime();
        LocalTime horaFin = horaInicioDeseada.plusMinutes(29);

        boolean dentroDeHorario = disponibilidad.stream().anyMatch(d ->
                !horaInicioDeseada.isBefore(d.getHoraInicio().toLocalTime()) &&
                        !horaFin.isAfter(d.getHoraFin().toLocalTime())
        );

        if (!dentroDeHorario) {
            throw new RuntimeException("La hora está fuera del horario de atención del médico");
        }
        // Validar que no se cruce con otra cita activa (no cancelada)
        List<Cita> citasEseDia = citasRepository.findByMedicoIdAndFecha(medicoId, fechaAgendada)
                .stream()
                .filter(c -> c.getEstado() != EstadoCita.CANCELADA) // Ignorar canceladas
                .toList();

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

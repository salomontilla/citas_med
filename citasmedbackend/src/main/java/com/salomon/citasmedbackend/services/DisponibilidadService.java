package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.EstadoCita;
import com.salomon.citasmedbackend.domain.disponibilidad.DiaSemana;
import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadDTO;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import com.salomon.citasmedbackend.domain.disponibilidad.UpdateDisponibilidadDTO;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.repository.CitaRepository;
import com.salomon.citasmedbackend.repository.DisponibilidadRepository;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.salomon.citasmedbackend.infra.utils.FechaUtils.convertirADiaSemana;
import static com.salomon.citasmedbackend.infra.utils.FechaUtils.convertirHora;

@Service
@RequiredArgsConstructor
public class DisponibilidadService {
    private final MedicoRepository medicoRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final CitaRepository citasRepository;

    public Disponibilidad agregarDisponibilidad(DisponibilidadDTO dto, Long medicoId) {
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));

        disponibilidadValidation(dto.horaInicio(), dto.horaFin(), medico, dto.diaSemana());

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

    public Page<Disponibilidad> obtenerDisponibilidadesPorMedico(Long medicoId, Pageable pageable) {
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));
        return disponibilidadRepository.findByMedicoId(medico.getId(), pageable);
    }

    public List<String> obtenerBloquesDisponibles(Long medicoId, LocalDate fecha) {
        DiaSemana diaSemana = convertirADiaSemana(fecha.getDayOfWeek());

        List<Disponibilidad> disponibilidades = disponibilidadRepository
                .findByMedicoIdAndDiaSemana(medicoId, diaSemana);

        // Obtener citas ya agendadas ese día
        List<Cita> citasEseDia = citasRepository.findActivasByMedicoAndFecha(medicoId, fecha);

        List<String> bloquesLibres = new ArrayList<>();

        for (Disponibilidad disponibilidad : disponibilidades) {
            LocalTime inicio = disponibilidad.getHoraInicio().toLocalTime();
            LocalTime fin = disponibilidad.getHoraFin().toLocalTime();

            // Dividir el bloque en segmentos de 30 minutos
            while (inicio.isBefore(fin)) {
                LocalTime siguiente = inicio.plusMinutes(30);
                LocalTime bloqueInicio = inicio;

                boolean ocupado = citasEseDia.stream().anyMatch(cita -> {
                    LocalTime citaHora = cita.getHora().toLocalTime();
                    return !citaHora.isBefore(bloqueInicio) && citaHora.isBefore(siguiente);
                });

                if (!ocupado) {
                    bloquesLibres.add(bloqueInicio.toString());
                }

                inicio = siguiente;
            }

        }

        return bloquesLibres;
    }


    public Disponibilidad modificarDisponibilidad(UpdateDisponibilidadDTO dto, Long id) {
        Disponibilidad disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        // Consolidar valores reales
        DiaSemana dia = dto.diaSemana() != null ? dto.diaSemana() : disponibilidad.getDiaSemana();
        String horaInicio = dto.horaInicio() != null ? dto.horaInicio() : disponibilidad.getHoraInicio().toString();
        String horaFin = dto.horaFin() != null ? dto.horaFin() : disponibilidad.getHoraFin().toString();

        // Validar con los valores consolidados
        disponibilidadValidation(horaInicio, horaFin, disponibilidad.getMedico(), dia);

        if (dto.diaSemana() != null) {
            disponibilidad.setDiaSemana(dto.diaSemana());
        }
        if (dto.horaInicio() != null) {
            disponibilidad.setHoraInicio(convertirHora(dto.horaInicio()));
        }
        if (dto.horaFin() != null) {
            disponibilidad.setHoraFin(convertirHora(dto.horaFin()));
        }

        return disponibilidadRepository.save(disponibilidad);
    }

    public String eliminarDisponibilidad(Long id) {
        Disponibilidad disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
        disponibilidadRepository.delete(disponibilidad);
        return "Disponibilidad eliminada correctamente.";
    }

    public void disponibilidadValidation (String hora, String horaFinal, Medico medico, DiaSemana diaSemana){
        LocalTime horaInicio = convertirHora(hora).toLocalTime();
        LocalTime horaFin = convertirHora(horaFinal).toLocalTime();

        // 1. Validar que la horaHora de inicio sea antes que la horaHora fin
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La horaHora de inicio debe ser anterior a la horaHora de fin.");
        }

        // 2. Validar que no se cruce con una disponibilidad ya existente
        List<Disponibilidad> existentes = disponibilidadRepository
                .findByMedicoIdAndDiaSemana(medico.getId(), diaSemana);

        boolean seCruza = existentes.stream().anyMatch(d ->
                horaInicio.isBefore(d.getHoraFin().toLocalTime()) &&
                        horaFin.isAfter(d.getHoraInicio().toLocalTime())
        );

        if (seCruza) {
            throw new IllegalArgumentException("Ya existe una disponibilidad para este médico que se cruza con la nueva.");
        }
    }

    public Disponibilidad obtenerPorId(Long id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
    }


}

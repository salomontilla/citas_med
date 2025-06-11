package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaActualizarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaRegisterDTO;
import com.salomon.citasmedbackend.domain.cita.EstadoCita;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.repository.CitasRepository;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import com.salomon.citasmedbackend.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {
    private final CitasRepository citasRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public Cita agendarCita( CitaRegisterDTO citaDto) {
        Paciente paciente = pacienteRepository.findByIdAndUsuarioActivo(citaDto.pacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));
        Medico medico = medicoRepository.findByIdAndUsuarioActivo(citaDto.medicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));

        Date formattedDate = convertirFecha(citaDto.fecha());
        Cita nuevaCita = new Cita(
                paciente,
                medico,
                formattedDate,
                Time.valueOf(citaDto.hora())
        );

        citasRepository.save(nuevaCita);
        return nuevaCita;
    }



    public Date convertirFecha(String fechaString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(fechaString, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa dd-MM-yyyy");
        }
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
        cita.actualizarCita(citaDto);
        citasRepository.save(cita);
        return cita;
    }

    public String eliminarCita(Long id) {
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
}

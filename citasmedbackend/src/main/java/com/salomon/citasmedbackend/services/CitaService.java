package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaRegisterDTO;
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
}

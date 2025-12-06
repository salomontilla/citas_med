package com.salomon.citasmedbackend;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaAgendarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaPacienteActualizarDTO;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.repository.CitaRepository;
import com.salomon.citasmedbackend.services.CitaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citasRepository;

    @InjectMocks
    private CitaService citaService;

    @Test
    void debeLanzarExcepcionCuandoHayCitaSolapada() {
        Long medicoId = 1L;
        LocalDate fecha = LocalDate.of(2025, 10, 16);
        LocalTime inicioNueva = LocalTime.of(10, 0);

        Cita citaExistente = new Cita();
        citaExistente.setId(50L);
        Paciente paciente = citaExistente.getPaciente();
        citaExistente.setHora(Time.valueOf("10:00:00"));
        CitaAgendarDTO citaAgendarDTO = new CitaAgendarDTO(medicoId, fecha.toString(), inicioNueva.toString());
        when(citasRepository.findByMedicoIdAndFecha(medicoId, fecha))
                .thenReturn(List.of(citaExistente));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                citaService.agendarCita(citaAgendarDTO, paciente.getUsuario().getEmail())
        );

        assertEquals("El médico ya tiene una cita en este horario", ex.getMessage());
    }

    @Test
    void noDebeLanzarExcepcionCuandoNoHaySolapamiento() {
        Long medicoId = 1L;
        LocalDate fecha = LocalDate.of(2025, 10, 22);
        LocalTime inicioNueva = LocalTime.of(11, 0);

        Cita citaExistente = new Cita();
        citaExistente.setId(2L);
        citaExistente.setHora(Time.valueOf("10:00:00"));

        Paciente paciente = citaExistente.getPaciente();

        CitaPacienteActualizarDTO citaPacienteActualizarDTO = new CitaPacienteActualizarDTO(null, inicioNueva.toString());
        when(citasRepository.findByMedicoIdAndFecha(medicoId, fecha))
                .thenReturn(List.of(citaExistente));

        assertDoesNotThrow(() ->
                citaService.actualizarCitaPaciente(citaExistente.getId(), citaPacienteActualizarDTO, paciente.getUsuario().getEmail())
        );
    }

}


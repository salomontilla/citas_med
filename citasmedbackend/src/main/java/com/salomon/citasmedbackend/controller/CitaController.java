package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaActualizarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaAgendarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaResponseDTO;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.CitaService;
import com.salomon.citasmedbackend.services.MedicoService;
import com.salomon.citasmedbackend.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed")
public class CitaController {
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    @PostMapping("/pacientes/citas/agendar")
    public ResponseEntity<CitaResponseDTO> agendarCita(@RequestBody @Valid CitaAgendarDTO citaDto) {
        Cita cita = citaService.agendarCita(citaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CitaResponseDTO(cita.getId(),
                cita.getPaciente().getId(),
                cita.getMedico().getId(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                cita.getEstado().toString()));
    }


    @GetMapping("/pacientes/mis-citas")
    public ResponseEntity<?> getCitasPacienteAuth(@AuthenticationPrincipal DetallesUsuario user) {
        Paciente paciente = pacienteService.obtenerPacientePorEmail(user.getUsername());
        List<Cita> citas = citaService.obtenerCitasPorPacienteId(paciente.getId());

        List<CitaResponseDTO> citasResponse = citas.stream()
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                )).toList();

        return ResponseEntity.ok(citasResponse);
    }



    @GetMapping("/medicos/mis-citas")
    public ResponseEntity<?> getCitasByMedicoId(@AuthenticationPrincipal DetallesUsuario user) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        List<Cita> citas = citaService.obtenerCitasPorMedicoId(medico.getId());

        List<CitaResponseDTO> citasResponse = citas.stream()
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                )).toList();

        return ResponseEntity.ok(citasResponse);

    }


    @PatchMapping("/editar-cita/{id}")
    @Transactional
    public ResponseEntity<?> updateCita(@PathVariable Long id,
                                        @RequestBody CitaActualizarDTO citaDto,
                                        @AuthenticationPrincipal DetallesUsuario user) {
        Cita cita = citaService.obtenerCitaById(id);

        String emailUsuario = user.getUsername();
        boolean esPaciente = cita.getPaciente().getUsuario().getEmail().equals(emailUsuario);
        boolean esMedico = cita.getMedico().getUsuario().getEmail().equals(emailUsuario);

        if (!esPaciente && !esMedico) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para editar esta cita");
        }

        Cita updatedCita = citaService.actualizarCita(id, citaDto);

        CitaResponseDTO response = new CitaResponseDTO(
                updatedCita.getId(),
                updatedCita.getPaciente().getId(),
                updatedCita.getMedico().getId(),
                updatedCita.getFecha().toString(),
                updatedCita.getHora().toString(),
                updatedCita.getEstado().toString()
        );

        return ResponseEntity.ok(response);
    }



}

package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.*;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.CitaService;
import com.salomon.citasmedbackend.services.MedicoService;
import com.salomon.citasmedbackend.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed")
public class CitaController {
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    @PostMapping("/pacientes/citas/agendar")
    public ResponseEntity<CitaResponseDTO> agendarCita(@RequestBody @Valid CitaAgendarDTO citaDto,
                                                       @AuthenticationPrincipal DetallesUsuario user) {
        Cita cita = citaService.agendarCita(citaDto, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CitaResponseDTO(cita.getId(),
                cita.getPaciente().getId(),
                cita.getMedico().getId(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                cita.getEstado().toString()));
    }


    @GetMapping("/pacientes/mis-citas")
    public ResponseEntity<?> getCitasPacienteAuth(@AuthenticationPrincipal DetallesUsuario user,
                                                  @PageableDefault(size = 10) Pageable pageable) {
        Paciente paciente = pacienteService.obtenerPacientePorEmail(user.getUsername());
        Page<Cita> citas = citaService.obtenerCitasPorPacienteId(paciente.getId(), pageable);

        Page<CitaResponseDTO> citasResponse = citas
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                ));

        return ResponseEntity.ok(citasResponse);
    }



    @GetMapping("/medicos/mis-citas")
    public ResponseEntity<?> getCitasByMedicoId(@AuthenticationPrincipal DetallesUsuario user,
                                                @PageableDefault(size = 10) Pageable pageable) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        Page<Cita> citas = citaService.obtenerCitasPorMedicoId(medico.getId(), pageable);

        Page<CitaResponseDTO> citasResponse = citas
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                ));

        return ResponseEntity.ok(citasResponse);

    }


    @PatchMapping("/pacientes/editar-cita/{id}")
    @Transactional
    public ResponseEntity<?> updateCita(@PathVariable Long id,
                                        @RequestBody CitaPacienteActualizarDTO citaDto,
                                        @AuthenticationPrincipal DetallesUsuario user) {

        Cita updatedCita = citaService.actualizarCitaPaciente(id, citaDto, user.getUsername());

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
    @PatchMapping("/pacientes/cancelar-cita/{id}")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id,
                                                  @AuthenticationPrincipal DetallesUsuario user) {
        Cita cita = citaService.obtenerCitaById(id);
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La cita ya está cancelada");
        }
        CitaResponseDTO response = new CitaResponseDTO(
                cita.getId(),
                cita.getPaciente().getId(),
                cita.getMedico().getId(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                EstadoCita.CANCELADA.toString()
        );
        return ResponseEntity.ok(response);
    }



}

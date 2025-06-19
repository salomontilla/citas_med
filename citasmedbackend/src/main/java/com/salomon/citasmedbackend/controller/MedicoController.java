package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.medico.ActualizarMedicoDTO;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.medico.MedicoResponseDTO;
import com.salomon.citasmedbackend.domain.medico.RegistrarMedicoDTO;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.paciente.PacientesResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/citasmed/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    // CRUD operations for Medico

    @GetMapping("/mis-datos")
    public ResponseEntity<MedicoResponseDTO> getMedicoById(@AuthenticationPrincipal DetallesUsuario user) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        return ResponseEntity.ok(new MedicoResponseDTO(
                medico.getId(),
                medico.getUsuario().getNombreCompleto(),
                medico.getUsuario().getEmail(),
                medico.getUsuario().getDocumento(),
                medico.getUsuario().getTelefono(),
                medico.getEspecialidad().toString()
        ));
    }

    @GetMapping("/mis-pacientes")
    public ResponseEntity<Page<PacientesResponseDTO>> getPacientesByMedico(@AuthenticationPrincipal DetallesUsuario user,
                                                                           @PageableDefault(size = 10) Pageable pageable) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        Page<Paciente> pacientes = medicoService.obtenerPacientesPorMedico(medico.getId(), pageable);
        return ResponseEntity.ok(pacientes.map(
                paciente -> new PacientesResponseDTO(
                        paciente.getId(),
                        paciente.getUsuario().getNombreCompleto(),
                        paciente.getUsuario().getDocumento(),
                        paciente.getUsuario().getEmail(),
                        paciente.getUsuario().getTelefono(),
                        paciente.getFechaNacimiento()
                )
        ));
    }

    @PatchMapping("/editar-perfil")
    @Transactional
    public ResponseEntity<?> updateMyInfo(@AuthenticationPrincipal DetallesUsuario user,
                                          @RequestBody ActualizarMedicoDTO medicoActualizarDTO) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        Medico medicoActualizado = medicoService.actualizarMedico(medico.getId(), medicoActualizarDTO);

        return ResponseEntity.ok(new MedicoResponseDTO(
                medicoActualizado.getId(),
                medicoActualizado.getUsuario().getNombreCompleto(),
                medicoActualizado.getUsuario().getEmail(),
                medicoActualizado.getUsuario().getDocumento(),
                medicoActualizado.getUsuario().getTelefono(),
                medicoActualizado.getEspecialidad().toString()
        ));
    }





}
